package com.team13.junction.model.ui

import com.team13.junction.model.SensorSubgroup
import com.team13.junction.model.ValueUnit
import java.time.LocalDate
import java.time.LocalDateTime

data class EventUiPage(
    val events: Map<LocalDate, List<EventUi>>,
)

data class EventUi(
    val name: String,
    val type: SensorSubgroup,
    val isEco: Boolean,
    val isAnomaly: Boolean,
    val message: String,
    val sensorName: String,
    val value: Double,
    val unit: ValueUnit,
    val blockName: String,
    val dateTime: LocalDateTime,
)
