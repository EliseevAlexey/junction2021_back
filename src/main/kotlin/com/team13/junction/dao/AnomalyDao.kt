package com.team13.junction.dao

import com.team13.junction.model.Anomaly
import org.springframework.data.jpa.repository.JpaRepository
import java.sql.Timestamp

interface AnomalyDao : JpaRepository<Anomaly, Long> {
    fun findByTimestampBetweenAndSensorIdIn(from: Timestamp, to: Timestamp, sensorIds: List<Long>): List<Anomaly>
}
