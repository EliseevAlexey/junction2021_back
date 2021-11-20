package com.team13.junction.model

import java.sql.Timestamp
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "water_forecast_2")
class WaterForecast(
    @Id val id: Int,
    val date: Timestamp,
    val sensorId: Long,
    val buildingId: Long,
    val blockId: Long,
    val waterNeutral: Double,
    val waterLower: Double,
    val waterUpper: Double,
)
