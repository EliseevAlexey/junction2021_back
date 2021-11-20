package com.team13.junction.model

import java.sql.Timestamp
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "water_forecast")
class WaterForecast(
    @Id val index: Long,
    val date: Timestamp,
    val sensorId: Long,
    val buildingId: Long,
    val blockId: Long,
    val coldWaterNeutral: Double?,
    val coldWaterLower: Double?,
    val coldWaterUpper: Double?,
    val hotWaterNeutral: Double?,
    val hotWaterLower: Double?,
    val hotWaterUpper: Double?,
)
