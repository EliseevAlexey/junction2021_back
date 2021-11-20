package com.team13.junction.service

import com.team13.junction.dao.SensorDao
import com.team13.junction.model.Block
import com.team13.junction.model.BlockData
import com.team13.junction.model.Building
import com.team13.junction.model.Sensor
import com.team13.junction.model.SensorDto
import com.team13.junction.model.SensorGroup
import com.team13.junction.model.ui.BlockUiDto
import com.team13.junction.model.ui.BuildingUiDto
import com.team13.junction.model.ui.Chart
import com.team13.junction.model.ui.EventUi
import com.team13.junction.model.ui.EventUiPage
import com.team13.junction.model.ui.MainUiPage
import com.team13.junction.model.ui.SensorUiDto
import com.team13.junction.web.SensorUtil
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class SensorService(
    private val dao: SensorDao,
    private val blockService: BlockService,
    private val waterStatsService: WaterStatsService,
    private val chartService: ChartService,
) {

    fun get(id: Long): SensorDto =
        getById(id)
            .toDto()

    @Transactional
    fun getData(id: Long, all: List<SensorGroup>, from: LocalDateTime, to: LocalDateTime): MainUiPage {
        val sensor = getById(id)
        val sensors = listOf(sensor)

        val block = sensor.block
        val building = block.building
        val blocksData = listOf(getBlockData(building, block, from, to))

        val buildingsData = listOf(
            BuildingUiDto(
                id = building.id,
                name = building.name,
                point = building.point,
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
        )
        return MainUiPage(
            buildings = buildingsData,
            totals = TotalExtractor.createTotals(buildingsData),
            eventPage = createEventPage(sensors, block, from, to)
        )
    }

    private fun getBlockData(
        building: Building,
        block: Block,
        from: LocalDateTime,
        to: LocalDateTime
    ): BlockData {
        val blockId = block.id
        val sensorDatas = block.sensors
            .map { sensor -> chartService.getSensorData(sensor, SensorUtil.ALL, building.id, blockId, from, to) }
        return BlockData(
            blockId = blockId,
            blockName = block.name,
            charts = BlockExtractor.extractBlockCharts(sensorDatas),
            sensorDatas = sensorDatas,
        )
    }

    private fun createEventPage(
        sensors: List<Sensor>,
        block: Block,
        from: LocalDateTime,
        to: LocalDateTime
    ): EventUiPage {

        val sensorsById = sensors.associateBy { it.id }
        val sensorIds = sensorsById.keys.toList()

        val stats = waterStatsService.getStats(sensorIds = sensorIds, from = from, to = to)

        val events = stats.map {
            EventUi(
                name = "Some event",
                sensorName = sensorsById.getValue(it.sensorId).name,
                value = it.value,
                blockName = block.name,
                dateTime = it.date,
            )
        }

        return EventUiPage(
            events = events.groupBy { it.dateTime.toLocalDate() }
        )
    }

    private fun Sensor.toDto(from: LocalDateTime? = null, to: LocalDateTime? = null) =
        SensorDto(
            id = id,
            name = name,
            type = sensorSubgroup,
            stats = if (from != null && to != null) getStats(this, from, to) else null
        )

    private fun getStats(sensor: Sensor, from: LocalDateTime, to: LocalDateTime): Chart =
        waterStatsService.getStats(sensor, from, to)

    fun getById(id: Long): Sensor =
        dao.findByIdOrNull(id) ?: throw EntityNotFound("Sensor with ID #$id not found")

    fun create(blockId: Long, dto: SensorDto): SensorDto {
        val block = blockService.get(blockId)

        return dao.save(
            Sensor(
                name = dto.name,
                sensorSubgroup = dto.type,
                block = block,
            )
        ).toDto()
    }

    @Transactional
    fun update(id: Long, blockDto: SensorDto): SensorDto =
        getById(id).let {
            it.name = blockDto.name
            dao.save(it)
        }.toDto()

    @Transactional
    fun delete(id: Long) {
        getById(id).let { dao.delete(it) }
    }

    fun getAll(): List<SensorDto> =
        dao.findAll()
            .map { it.toDto() }

}

