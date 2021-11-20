package com.team13.junction.model

import java.io.Serializable
import java.sql.Timestamp
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table

@Entity
@Table(name = "forecast")
@IdClass(value = ForecastId::class)
class Forecast(
    @Id @Column(name = "ds") val dateTime: Timestamp,
    @Id val sensorId: Long,
    val buildingId: Long,
    val blockId: Long,
    @Column(name = "yhat") val value: Double,
    @Column(name = "yhat_lower") val yhatLower: Double,
    @Column(name = "yhat_upper") val yhatUpper: Double,
)

data class ForecastId(
    val dateTime: Timestamp? = null,
    val sensorId: Long? = null,
) : Serializable
