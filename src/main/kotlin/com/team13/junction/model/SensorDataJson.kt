package com.team13.junction.model

import com.fasterxml.jackson.annotation.JsonAlias
import java.math.BigDecimal

data class SensorDataJson(
    @JsonAlias("Consumption") val consumption: BigDecimal,
    @JsonAlias("Temp") val temp: BigDecimal,
    @JsonAlias("FlowTime") val flowTime: BigDecimal,
    @JsonAlias("Power_Consumption") val powerConsumption: BigDecimal,
)
