package com.team13.junction.util

import com.team13.junction.model.SensorGroup
import com.team13.junction.model.ValueUnit

object UnitConverter {

    fun SensorGroup.toUnit() =
        when (this) {
            SensorGroup.WATER_COLD, SensorGroup.WATER_HOT -> ValueUnit.LITERS
            SensorGroup.ENERGY -> ValueUnit.KWT_PER_HOUR
        }

}
