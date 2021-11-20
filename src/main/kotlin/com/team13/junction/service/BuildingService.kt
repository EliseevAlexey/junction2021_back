package com.team13.junction.service

import com.team13.junction.dao.BuildingDao
import com.team13.junction.model.Building
import com.team13.junction.model.BuildingDto
import com.team13.junction.model.Sensor
import com.team13.junction.model.SensorGroup
import com.team13.junction.model.SensorGroup.*
import com.team13.junction.model.SensorSubgroup
import com.team13.junction.model.ui.BlockUiDto
import com.team13.junction.model.ui.BuildingUiDto
import com.team13.junction.model.ui.Chart
import com.team13.junction.model.ui.ChartItem
import com.team13.junction.model.ui.MainUiPage
import com.team13.junction.model.ui.SensorUiDto
import com.team13.junction.model.ui.TotalUiDto
import com.team13.junction.service.ThresholdService.getThreshold
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class BuildingService(
    private val dao: BuildingDao,
    private val waterStatsService: WaterStatsService,
) {

    fun get(id: Long): Building =
        dao.findByIdOrNull(id) ?: throw EntityNotFound("Building with ID #$id not found")

    fun create(dto: BuildingDto) =
        dao.save(
            Building(
                name = dto.name,
            )
        )

    @Transactional
    fun update(id: Long, dto: BuildingDto): Building =
        get(id).let {
            it.name = dto.name
            dao.save(it)
        }

    @Transactional
    fun delete(id: Long) {
        get(id).let { dao.delete(it) }
    }

    fun getAll(): List<Building> =
        dao.findAll()

    @Transactional
    fun getData(
        groups: List<SensorGroup>,
        from: LocalDateTime,
        to: LocalDateTime
    ): MainUiPage {
        val buildingsData = getAll().map { building ->
            val buildingId = building.id
            val buildingName = building.name
            val buildingPoint = building.point
            val blocksData = building.blocks.map { block ->
                val blockId = block.id
                val sensorDatas = block.sensors
                    .map { sensor -> getSensorData(sensor, groups, buildingId, blockId, from, to) }
                BlockData(
                    blockId = blockId,
                    blockName = block.name,
                    charts = extractBlockCharts(sensorDatas),
                    sensorDatas = sensorDatas,
                )
            }

            BuildingUiDto(
                id = buildingId,
                name = buildingName,
                point = buildingPoint,
                blocks = blocksData.map { blockData ->
                    BlockUiDto(
                        id = blockData.blockId,
                        name = blockData.blockName,
                        charts = blockData.charts,
                        sensors = blockData.sensorDatas.map { sensorData ->
                            SensorUiDto(
                                id = sensorData.sensorId,
                                name = sensorData.sensorName,
                                charts = sensorData.charts,
                            )
                        }
                    )
                }
            )
        }
        return MainUiPage(
            buildings = buildingsData,
            totals = createTotals(buildingsData)
        )
    }

    private fun createTotals(buildingsData: List<BuildingUiDto>): List<TotalUiDto> {

        return emptyList()
    }

    private data class BlockData(
        val blockId: Long,
        val blockName: String,
        val charts: Map<SensorGroup, Chart>,
        val sensorDatas: List<SensorData>,
    )

    private fun getSensorData(
        sensor: Sensor,
        groups: List<SensorGroup>,  // For choosing Stats provider
        buildingId: Long,
        blockId: Long,
        from: LocalDateTime,
        to: LocalDateTime
    ): SensorData {
        val sensorId = sensor.id
        val sensorSubgroup = sensor.sensorSubgroup
        val sensorCharts =
            groups.associateWith { getChart(groups, sensorSubgroup, buildingId, blockId, sensorId, from, to) }

        return SensorData(
            sensorId = sensorId,
            sensorName = sensor.name,
            charts = sensorCharts
        )
    }

    private data class SensorData(
        val sensorId: Long,
        val sensorName: String,
        val charts: Map<SensorGroup, Chart>
    )

    // FIXME hardcode
    private fun extractBlockCharts(charts: List<SensorData>): Map<SensorGroup, Chart> {
        val hotList = mutableListOf<Chart>()
        val coldList = mutableListOf<Chart>()
        val energyList = mutableListOf<Chart>()

        charts.forEach { sensorData ->
            sensorData.charts.forEach { (key, value) ->
                when (key) {
                    WATER_HOT -> hotList.add(value)
                    WATER_COLD -> coldList.add(value)
                    else -> energyList.add(value)
                }
            }
        }

        return listOfNotNull(
            if (hotList.isNotEmpty()) {
                WATER_HOT to Chart(
                    threshold = hotList.sumOf { it.threshold },
                    data = hotList.flatMap { it.data }.sortedBy { it.date })
            } else null,
            if (coldList.isNotEmpty()) {
                WATER_COLD to Chart(
                    threshold = coldList.sumOf { it.threshold },
                    data = coldList.flatMap { it.data }.sortedBy { it.date })
            } else null,
            if (energyList.isNotEmpty()) {
                ENERGY to Chart(
                    threshold = energyList.sumOf { it.threshold },
                    data = energyList.flatMap { it.data }.sortedBy { it.date })
            } else null,
        ).toMap()
    }

    private fun getChart(
        sensorGroups: List<SensorGroup>, // For choosing Stats provider
        sensorSubgroup: SensorSubgroup,
        buildingId: Long,
        blockId: Long,
        sensorId: Long,
        from: LocalDateTime,
        to: LocalDateTime
    ) =
        Chart(
            threshold = getThreshold(subgroup = sensorSubgroup),
            data = getStats(sensorGroups, buildingId, blockId, sensorId, from, to)
        )


    fun getData(id: Long, groups: List<SensorGroup>, from: LocalDateTime, to: LocalDateTime): MainUiPage {
        return MainUiPage(
            buildings = emptyList(),
            totals = emptyList()
        )
    }

    private fun getStats(
        groups: List<SensorGroup>,
        buildingId: Long,
        blockId: Long,
        sensorId: Long,
        from: LocalDateTime,
        to: LocalDateTime,
    ): List<ChartItem> =
        if (groups.containsAll(listOf(WATER_COLD, WATER_HOT))) {
            waterStatsService.getStats(
                buildingId = buildingId,
                blockId = blockId,
                sensorId = sensorId,
                from = from,
                to = to,
            )
        } else throw UnsupportedOperationException("$groups stats is not supported")

}
