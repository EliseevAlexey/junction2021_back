package com.team13.junction.service

import com.team13.junction.dao.BlockDao
import com.team13.junction.model.Block
import com.team13.junction.model.BlockData
import com.team13.junction.model.BlockDto
import com.team13.junction.model.ChartData
import com.team13.junction.model.Sensor
import com.team13.junction.model.SensorGroup
import com.team13.junction.model.ui.BlockUiDto
import com.team13.junction.model.ui.BuildingUiDto
import com.team13.junction.model.ui.EventUi
import com.team13.junction.model.ui.EventUiPage
import com.team13.junction.model.ui.MainUiPage
import com.team13.junction.model.ui.SensorUiDto
import com.team13.junction.model.ui.toPointDto
import com.team13.junction.service.BlockExtractor.extractBlockCharts
import com.team13.junction.util.UnitConverter.toUnit
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

        // prepare data
        val sensorsById = sensors.associateBy { it.id }
        val sensorIds = sensorsById.keys.toList()
        val stats = waterStatsService.getStats(sensorIds = sensorIds, from = from, to = to)
        // prepare data


        val blocksData = listOf(getBlockData(block, stats))
        val buildingsData = listOf(
            BuildingUiDto(
                id = building.id,
                name = building.name,
                point = building.point?.toPointDto(),
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
        )
        return MainUiPage(
            buildings = buildingsData,
            totals = TotalExtractor.createTotals(buildingsData),
            eventPage = createEventPage(block, stats, sensorsById)
        )
    }

    private fun getBlockData(
        block: Block,
        stats: List<ChartData>,
    ): BlockData {
        val blockId = block.id
        val sensorDatas = block.sensors
            .map { sensor -> chartService.getSensorData(sensor, ALL, stats) }
        return BlockData(
            blockId = blockId,
            blockName = block.name,
            charts = extractBlockCharts(sensorDatas),
            sensorDatas = sensorDatas,
        )
    }

    private fun createEventPage(
        block: Block,
        stats: List<ChartData>,
        sensorsById: Map<Long, Sensor>,
    ): EventUiPage {

        val events = stats.map {
            val sensor = sensorsById.getValue(it.sensorId)
            val threshold = ThresholdService.getThreshold(sensor.sensorSubgroup)
            val currentValue = it.value
            val isEco = currentValue < threshold
            EventUi(
                name = "Some event",
                type = sensor.sensorSubgroup,
                sensorName = sensor.name,
                value = currentValue,
                unit = sensor.sensorSubgroup.group.toUnit(),
                blockName = block.name,
                dateTime = it.date,
                message = if (isEco) "OK" else "Think more ECO-way",
                isEco = isEco,
                isAnomaly = false, // TODO
            )
        }

        return EventUiPage(
            events = events.groupBy { it.dateTime.toLocalDate() }
        )
    }

}
