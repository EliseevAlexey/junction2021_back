package com.team13.junction.dao

import com.team13.junction.model.SensorData
import org.springframework.data.jpa.repository.JpaRepository
import java.sql.Timestamp
import java.time.LocalDateTime

interface SensorDataDao : JpaRepository<SensorData, Long> {
    fun findByTimestampBetweenAndSensorIdIn(from: LocalDateTime, to: LocalDateTime, sensorIds: List<Long>): List<SensorData>
}
