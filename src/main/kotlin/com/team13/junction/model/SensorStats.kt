package com.team13.junction.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class SensorStats(
    val threshold: BigDecimal,
    val data: List<StatItem>,
)

data class StatItem(
    val date: LocalDateTime,
    val value: Double,
)