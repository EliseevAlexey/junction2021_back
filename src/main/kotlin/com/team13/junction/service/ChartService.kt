package com.team13.junction.service

import com.team13.junction.model.BlockData
import com.team13.junction.model.ChartData
import com.team13.junction.model.Sensor
import com.team13.junction.model.SensorGroup
import com.team13.junction.model.SensorModel
import com.team13.junction.model.SensorSubgroup
import com.team13.junction.model.ui.Chart
import com.team13.junction.model.ui.ChartItem
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ChartService(private val waterStatsService: WaterStatsService) {

    fun getSensorData(
        sensor: Sensor,
        groups: List<SensorGroup>,  // For choosing Stats provider
        stats: List<ChartData>,
    ): SensorModel {
        val sensorId = sensor.id
        val sensorSubgroup = sensor.sensorSubgroup
        val sensorCharts =
            groups.associateWith { getChart(sensorSubgroup, stats) }

        return SensorModel(
            sensorId = sensorId,
            sensorName = sensor.name,
            charts = sensorCharts,
            sensorSubgroup = sensorSubgroup
        )
    }

    private fun getChart(
        sensorSubgroup: SensorSubgroup,
        stats: List<ChartData>,
    ) =
        Chart(
            threshold = ThresholdService.getThreshold(subgroup = sensorSubgroup),
            data = stats.map {
                ChartItem(
                    date = it.date,
                    value = it.value,
                )
            }
        )

    fun extractBuildingCharts(blocksData: List<BlockData>): Map<SensorGroup, Chart> {
        val hotList = mutableListOf<Chart>()
        val coldList = mutableListOf<Chart>()
        val energyList = mutableListOf<Chart>()

        blocksData.forEach { blockData ->
            blockData.charts.forEach { (key, value) ->
                when (key) {
                    SensorGroup.WATER_HOT -> hotList.add(value)
                    SensorGroup.WATER_COLD -> coldList.add(value)
                    else -> energyList.add(value)
                }
            }
        }

        return listOfNotNull(
            if (hotList.isNotEmpty()) {
                SensorGroup.WATER_HOT to Chart(
                    threshold = hotList.sumOf { it.threshold },
                    data = hotList.flatMap { it.data }.sortedBy { it.date })
            } else null,
            if (coldList.isNotEmpty()) {
                SensorGroup.WATER_COLD to Chart(
                    threshold = coldList.sumOf { it.threshold },
                    data = coldList.flatMap { it.data }.sortedBy { it.date })
            } else null,
            if (energyList.isNotEmpty()) {
                SensorGroup.ENERGY to Chart(
                    threshold = energyList.sumOf { it.threshold },
                    data = energyList.flatMap { it.data }.sortedBy { it.date })
            } else null,
        ).toMap()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ChartService::class.java)
    }
}
