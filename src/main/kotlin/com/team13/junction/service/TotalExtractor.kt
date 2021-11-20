package com.team13.junction.service

import com.team13.junction.model.SensorGroup
import com.team13.junction.model.ui.BuildingUiDto
import com.team13.junction.model.ui.TotalUiDto
import com.team13.junction.util.UnitConverter.toUnit

object TotalExtractor {

    fun createTotals(buildingsData: List<BuildingUiDto>): List<TotalUiDto> {
        val totals = mutableListOf<TotalUiDto>()

        var totalHot = 0.0
        var totalCold = 0.0
        var totalEnergy = 0.0

        buildingsData.forEach { buildingUiDto ->
            buildingUiDto.blocks.forEach { blockUiDto ->
                blockUiDto.charts.forEach { (sensorGroup, chart) ->
                    when (sensorGroup) {
                        SensorGroup.WATER_HOT -> totalHot += chart.data.sumOf { it.value }
                        SensorGroup.WATER_COLD -> totalCold += chart.data.sumOf { it.value }
                        SensorGroup.ENERGY -> totalEnergy += chart.data.sumOf { it.value }
                    }
                }
            }
        }
        if (totalHot != 0.0) addTotals(SensorGroup.WATER_HOT, totals, totalHot)
        if (totalCold != 0.0) addTotals(SensorGroup.WATER_COLD, totals, totalCold)
        if (totalEnergy != 0.0) addTotals(SensorGroup.ENERGY, totals, totalEnergy)

        return totals
    }

    private fun addTotals(
        sensorGroup: SensorGroup,
        totals: MutableList<TotalUiDto>,
        buildingsData: Double
    ) {
        totals.add(
            TotalUiDto(
                sensorGroup = sensorGroup,
                values = buildingsData,
                unit = sensorGroup.toUnit()
            )
        )
    }

}