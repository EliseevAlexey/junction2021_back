package com.team13.junction.service

import com.team13.junction.dao.BuildingDao
import com.team13.junction.model.Block
import com.team13.junction.model.BlockData
import com.team13.junction.model.Building
import com.team13.junction.model.BuildingDto
import com.team13.junction.model.ChartData
import com.team13.junction.model.Sensor
import com.team13.junction.model.SensorGroup
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
import com.team13.junction.model.ui.toPointDto
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

        // Prepare data
        val sensorIds = buildings.flatMap { building ->
            building.blocks.flatMap { block ->
                block.sensors.map { it.id }
            }
        }
        val blocks = buildings.flatMap { it.blocks }
        val blocksById = blocks.associateBy { it.id }
        val sensors = blocks.flatMap { it.sensors }
        val sensorsById = sensors.associateBy { it.id }
        val stats = waterStatsService.getStats(sensorIds, from, to)
        // Prepare data


        val buildingsData = buildings.map { building ->
            getBuildingData(building, groups, stats)
        }
        return MainUiPage(
            buildings = buildingsData,
            totals = createTotals(buildingsData),
            eventPage = getEvents(stats, sensors, sensorsById, blocksById)
        )
    }

    private fun getEvents(
        stats: List<ChartData>,
        sensors: List<Sensor>,
        sensorsById: Map<Long, Sensor>,
        blocksById: Map<Long, Block>
    ): EventUiPage {


        val events = stats.map {
            val threshold = getThreshold(sensors[0].sensorSubgroup)
            val currentValue = it.value
            val isEco = currentValue < threshold
            val sensor = sensorsById.getValue(it.sensorId)
            EventUi(
                name = "Some event",
                sensorName = sensor.name,
                value = currentValue,
                type = sensor.sensorSubgroup,
                unit = sensor.sensorSubgroup.group.toUnit(),
                blockName = blocksById.getValue(it.blockId).name,
                dateTime = it.date,
                message = if (isEco) "OK" else "Think more ECO-way",
                isEco = isEco,
                isAnomaly = false, // FIXME
            )
        }

        return EventUiPage(
            events = events.groupBy { it.dateTime.toLocalDate() }
        )
    }

    private fun getBuildingData(
        building: Building,
        groups: List<SensorGroup>,
        stats: List<ChartData>
    ): BuildingUiDto {
        val buildingId = building.id
        val buildingName = building.name
        val buildingPoint = building.point
        val blocksData = building.blocks.map { block ->
            val blockId = block.id
            val sensorDatas = block.sensors
                .map { sensor -> getSensorData(sensor, groups, stats) }
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
            point = buildingPoint?.toPointDto(),
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
                            type = sensorData.sensorSubgroup
                        )
                    }
                )
            }
        )
    }

    private fun getSensorData(
        sensor: Sensor,
        groups: List<SensorGroup>,  // For choosing Stats provider
        stats: List<ChartData>
    ): SensorModel {
        val sensorId = sensor.id
        val sensorSubgroup = sensor.sensorSubgroup
        val sensorCharts =
            groups.associateWith { getChart(sensorSubgroup, stats) }

        return SensorModel(
            sensorId = sensorId,
            sensorName = sensor.name,
            sensorSubgroup = sensor.sensorSubgroup,
            charts = sensorCharts,
        )
    }

    private fun getChart(
        sensorSubgroup: SensorSubgroup,
        stats: List<ChartData>
    ) =
        Chart(
            threshold = getThreshold(subgroup = sensorSubgroup),
            data = stats.map {
                ChartItem(
                    date = it.date,
                    value = it.value,
                )
            }
        )

    @Transactional
    fun getData(id: Long, groups: List<SensorGroup>, from: LocalDateTime, to: LocalDateTime): MainUiPage {
        val buildings = listOf(get(id))

        // Prepare data
        val sensorIds = buildings.flatMap { building ->
            building.blocks.flatMap { block ->
                block.sensors.map { it.id }
            }
        }
        val blocks = buildings.flatMap { it.blocks }
        val blocksById = blocks.associateBy { it.id }
        val sensors = blocks.flatMap { it.sensors }
        val sensorsById = sensors.associateBy { it.id }
        val stats = waterStatsService.getStats(sensorIds, from, to)
        // Prepare data


        val buildingsData = buildings.map { building ->
            getBuildingData(building, groups, stats)
        }
        return MainUiPage(
            buildings = buildingsData,
            totals = createTotals(buildingsData),
            eventPage = EventUiPage(events = emptyMap()), // FIXME
        )
    }

    companion object {
        private val logger = LoggerFactory.getLogger(BuildingService::class.java)
    }
}
