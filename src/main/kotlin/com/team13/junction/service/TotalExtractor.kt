package com.team13.junction.service

import com.team13.junction.model.SensorGroup.ENERGY
import com.team13.junction.model.SensorGroup.WATER_COLD
import com.team13.junction.model.SensorGroup.WATER_HOT
import com.team13.junction.model.ValueUnit
import com.team13.junction.model.ui.BuildingUiDto
import com.team13.junction.model.ui.TotalUiDto
import com.team13.junction.util.UnitConverter.toMoney
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
                        WATER_HOT -> totalHot += chart.data.sumOf { it.value }
                        WATER_COLD -> totalCold += chart.data.sumOf { it.value }
                        ENERGY -> totalEnergy += chart.data.sumOf { it.value }
                    }
                }
            }
        }

        val waterSum = totalHot + totalCold
        if (waterSum != 0.0) {
            totals.add(
                TotalUiDto(
                    sensorGroup = WATER_HOT,
                    values = waterSum,
                    unit = WATER_HOT.toUnit()
                )
            )
            totals.add(
                TotalUiDto(
                    sensorGroup = WATER_HOT,
                    values = totalHot * WATER_HOT.toMoney() + totalCold * WATER_COLD.toMoney(),
                    unit = ValueUnit.EURO,
                )
            )
        }
        if (totalEnergy != 0.0) {
            totals.add(
                TotalUiDto(
                    sensorGroup = ENERGY,
                    values = totalEnergy,
                    unit = ENERGY.toUnit()
                )
            )
            totals.add(
                TotalUiDto(
                    sensorGroup = ENERGY,
                    values = totalEnergy * ENERGY.toMoney(),
                    unit = ValueUnit.EURO,
                )
            )
        }
        return totals
    }

}