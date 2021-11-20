package com.team13.junction.service

import com.team13.junction.dao.BuildingDao
import com.team13.junction.model.BlockData
import com.team13.junction.model.Building
import com.team13.junction.model.BuildingDto
import com.team13.junction.model.Sensor
import com.team13.junction.model.SensorGroup
import com.team13.junction.model.SensorGroup.ENERGY
import com.team13.junction.model.SensorGroup.WATER_COLD
import com.team13.junction.model.SensorGroup.WATER_HOT
import com.team13.junction.model.SensorModel
import com.team13.junction.model.SensorSubgroup
import com.team13.junction.model.ui.BlockUiDto
import com.team13.junction.model.ui.BuildingUiDto
import com.team13.junction.model.ui.Chart
import com.team13.junction.model.ui.ChartItem
import com.team13.junction.model.ui.EventUi
import com.team13.junction.model.ui.EventUiPage
import com.team13.junction.model.ui.MainUiPage
import com.team13.junction.model.ui.SensorUiDto
import com.team13.junction.model.ui.TotalUiDto
import com.team13.junction.service.BlockExtractor.extractBlockCharts
import com.team13.junction.service.ThresholdService.getThreshold
import com.team13.junction.service.TotalExtractor.createTotals
import com.team13.junction.util.UnitConverter.toUnit
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class BuildingService(
    private val dao: BuildingDao,
    private val waterStatsService: WaterStatsService,
    private val chartService: ChartService,
) {

    fun get(id: Long): Building =
        dao.findByIdOrNull(id) ?: throw EntityNotFound("Building with ID #$id not found")

    fun getByBlockId(blockId: Long): Building {
        return dao.findAll()
            .find { blockId in it.blocks.map { it.id } }
            ?: throw EntityNotFound("Building with Block ID #$blockId not found")
    }

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

        val buildings = getAll()
        val buildingsData = buildings.map { building ->
            getBuildingData(building, groups, from, to)
        }
        return MainUiPage(
            buildings = buildingsData,
            totals = createTotals(buildingsData),
            eventPage = getEvents(buildings, from, to)
        )
    }

    private fun getEvents(buildings: List<Building>, from: LocalDateTime, to: LocalDateTime): EventUiPage {
        val sensorIds = buildings.flatMap { building ->
            building.blocks.flatMap { block ->
                block.sensors.map { sensor ->
                    sensor.id
                }
            }
        }
        val blocks = buildings.flatMap { it.blocks }
        val blocksById = blocks.associateBy { it.id }
        val sensors = blocks.flatMap { it.sensors }
        val sensorsById = sensors.associateBy { it.id }


        val stats = waterStatsService.getStats(sensorIds, from, to)


        val events = stats.map {
            EventUi(
                name = "Some event",
                sensorName = sensorsById.getValue(it.sensorId).name,
                value = it.value,
                blockName = blocksById.getValue(it.blockId).name,
                dateTime = it.date,
            )
        }

        return EventUiPage(
            events = events.groupBy { it.dateTime.toLocalDate() }
        )
    }

    private fun getBuildingData(
        building: Building,
        groups: List<SensorGroup>,
        from: LocalDateTime,
        to: LocalDateTime
    ): BuildingUiDto {
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

        return BuildingUiDto(
            id = buildingId,
            name = buildingName,
            point = buildingPoint,
            charts = chartService.extractBuildingCharts(blocksData),
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

    private fun getSensorData(
        sensor: Sensor,
        groups: List<SensorGroup>,  // For choosing Stats provider
        buildingId: Long,
        blockId: Long,
        from: LocalDateTime,
        to: LocalDateTime
    ): SensorModel {
        val sensorId = sensor.id
        val sensorSubgroup = sensor.sensorSubgroup
        val sensorCharts =
            groups.associateWith { getChart(groups, sensorSubgroup, buildingId, blockId, sensorId, from, to) }

        return SensorModel(
            sensorId = sensorId,
            sensorName = sensor.name,
            charts = sensorCharts
        )
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

    @Transactional
    fun getData(id: Long, groups: List<SensorGroup>, from: LocalDateTime, to: LocalDateTime): MainUiPage {
        val buildingsData = listOf(getBuildingData(get(id), groups, from, to))
        return MainUiPage(
            buildings = buildingsData,
            totals = createTotals(buildingsData),
            eventPage = EventUiPage(events = emptyMap()), // FIXME
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
        } else {
            logger.error("$groups not supported")
            emptyList()
        }

    companion object {
        private val logger = LoggerFactory.getLogger(BuildingService::class.java)
    }
}
