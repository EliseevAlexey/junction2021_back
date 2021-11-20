package com.team13.junction.model.ui

import java.time.LocalDate
import java.time.LocalDateTime

data class EventUiPage(
    val events: Map<LocalDate, List<EventUi>>,
)

data class EventUi(
    val name: String,
    val sensorName: String,
    val value: Double,
    val blockName: String,
    val dateTime: LocalDateTime,
)
