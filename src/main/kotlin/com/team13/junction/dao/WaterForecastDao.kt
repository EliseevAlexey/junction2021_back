package com.team13.junction.dao

import com.team13.junction.model.WaterForecast
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.sql.Timestamp

interface WaterForecastDao : JpaRepository<WaterForecast, Long> {
    @Query(
        value = "select * from water_forecast_2 where date between :from and :to and building_id = :buildingId and block_id = :blockId and sensor_id = :sensorId",
        nativeQuery = true
    )
    fun findBy(
        from: Timestamp,
        to: Timestamp,
        buildingId: Long,
        blockId: Long,
        sensorId: Long
    ): List<WaterForecast>

    fun findByDateBetweenAndSensorIdIn(from: Timestamp, to: Timestamp, sensorIds: List<Long>): List<WaterForecast>
}
