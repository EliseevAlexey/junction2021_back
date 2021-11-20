package com.team13.junction.model

import com.team13.junction.model.ui.Chart

data class SensorModel(
    val sensorId: Long,
    val sensorName: String,
    val charts: Map<SensorGroup, Chart>
)
