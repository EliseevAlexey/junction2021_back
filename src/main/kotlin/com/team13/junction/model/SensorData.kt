package com.team13.junction.model

import com.fasterxml.jackson.annotation.JsonAlias
import java.time.LocalDateTime

data class SensorData(
    @JsonAlias("Consumption") val consumption: Double,
    @JsonAlias("Temp") val temp: Double,
    @JsonAlias("FlowTime") val flowTime: Double,
    @JsonAlias("Power_Consumption") val powerConsumption: Double,
    @JsonAlias("TimeStamp") val timeStamp: LocalDateTime,
)