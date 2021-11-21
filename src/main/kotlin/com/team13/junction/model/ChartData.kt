package com.team13.junction.model

import java.time.LocalDateTime

data class ChartData(
    val sensorId: Long,
    val blockId: Long,
    val date: LocalDateTime,
    val value: Double,
)
