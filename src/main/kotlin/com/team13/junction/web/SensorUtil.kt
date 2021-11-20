package com.team13.junction.web

import com.team13.junction.model.SensorGroup

object SensorUtil {
    val ALL = SensorGroup.values().toList()
    val WATER = listOf(SensorGroup.WATER_COLD, SensorGroup.WATER_HOT)
    val ENERGY = listOf(SensorGroup.ENERGY)
}