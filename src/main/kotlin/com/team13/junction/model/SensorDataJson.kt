package com.team13.junction.model

import com.fasterxml.jackson.annotation.JsonAlias
import java.math.BigDecimal

class SensorDataJson(
    @JsonAlias("Consumption") val consumption: Double,
    @JsonAlias("Temp") val temp: BigDecimal,
    @JsonAlias("FlowTime") val flowTime: BigDecimal,
    @JsonAlias("Power_Consumption") val powerConsumption: BigDecimal,
)
