package com.team13.junction.model

import com.fasterxml.jackson.annotation.JsonAlias
import java.math.BigDecimal
import java.time.LocalDateTime

data class SensorData(
    @JsonAlias("Consumption") val consumption: BigDecimal,
    @JsonAlias("Temp") val temp: BigDecimal,
    @JsonAlias("FlowTime") val flowTime: BigDecimal,
    @JsonAlias("Power_Consumption") val powerConsumption: BigDecimal,
    @JsonAlias("TimeStamp") val timeStamp: LocalDateTime,
)
