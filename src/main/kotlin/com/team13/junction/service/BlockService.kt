package com.team13.junction.service

import com.team13.junction.dao.BlockDao
import com.team13.junction.model.Block
import com.team13.junction.model.BlockData
import com.team13.junction.model.BlockDto
import com.team13.junction.model.Building
import com.team13.junction.model.Sensor
import com.team13.junction.model.SensorGroup
import com.team13.junction.model.ui.BlockUiDto
import com.team13.junction.model.ui.BuildingUiDto
import com.team13.junction.model.ui.EventUi
import com.team13.junction.model.ui.EventUiPage
import com.team13.junction.model.ui.MainUiPage
import com.team13.junction.model.ui.SensorUiDto
import com.team13.junction.service.BlockExtractor.extractBlockCharts
import com.team13.junction.web.SensorUtil.ALL
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class BlockService(
    private val dao: BlockDao,
    private val buildingService: BuildingService,
    private val waterStatsService: WaterStatsService,
    private val chartService: ChartService,
) {

    fun get(id: Long): Block =
        dao.findByIdOrNull(id) ?: throw EntityNotFound("Block with ID #$id not found")

    fun create(buildingId: Long, dto: BlockDto): Block {
        val building = buildingService.get(buildingId)

        return dao.save(
            Block(
                name = dto.name,
                building = building,
            )
        )
    }

    @Transactional
    fun update(id: Long, blockDto: BlockDto): Block =
        get(id).let {
            it.name = blockDto.name
            dao.save(it)
        }

    @Transactional
    fun delete(id: Long) {
        get(id).let { dao.delete(it) }
    }

    fun getAll(): List<Block> =
        dao.findAll()

    @Transactional
    fun getData(id: Long, all: List<SensorGroup>, from: LocalDateTime, to: LocalDateTime): MainUiPage {
        val block = get(id)
        val sensors = block.sensors

        val building = sensors.first().block.building

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
            .map { sensor -> chartService.getSensorData(sensor, ALL, building.id, blockId, from, to) }
        return BlockData(
            blockId = blockId,
            blockName = block.name,
            charts = extractBlockCharts(sensorDatas),
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

}
