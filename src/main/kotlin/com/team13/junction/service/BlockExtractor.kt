package com.team13.junction.service

import com.team13.junction.model.SensorGroup
import com.team13.junction.model.SensorModel
import com.team13.junction.model.ui.Chart

object BlockExtractor {

    // FIXME hardcode
    fun extractBlockCharts(charts: List<SensorModel>): Map<SensorGroup, Chart> {
        val hotList = mutableListOf<Chart>()
        val coldList = mutableListOf<Chart>()
        val energyList = mutableListOf<Chart>()

        charts.forEach { sensorData ->
            sensorData.charts.forEach { (key, value) ->
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
}