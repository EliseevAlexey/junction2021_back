package com.team13.junction.util

import com.team13.junction.model.SensorGroup
import com.team13.junction.model.ValueUnit

object UnitConverter {

    fun SensorGroup.toUnit() =
        when (this) {
            SensorGroup.WATER_COLD, SensorGroup.WATER_HOT -> ValueUnit.LITERS
            SensorGroup.ENERGY -> ValueUnit.KWT_PER_HOUR
        }

    fun SensorGroup.toMoney() =
        when (this) {
            SensorGroup.WATER_COLD -> COLD_WATER_PRICE
            SensorGroup.WATER_HOT -> HOT_WATER_PRICE
            SensorGroup.ENERGY -> ENERGY_PRICE
        }

    private const val COLD_WATER_PRICE = 1.2
    private const val HOT_WATER_PRICE = 1.2
    private const val ENERGY_PRICE = 1.2
}
