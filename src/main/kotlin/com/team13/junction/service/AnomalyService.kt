package com.team13.junction.service

import com.team13.junction.dao.AnomalyDao
import com.team13.junction.model.Anomaly
import com.team13.junction.util.toTimestamp
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AnomalyService(private val dao: AnomalyDao) {

    fun get(from: LocalDateTime, to: LocalDateTime, sensorIds: List<Long>): List<Anomaly> =
        dao.findByTimestampBetweenAndSensorIdIn(
            from = from.toTimestamp(),
            to = to.toTimestamp(),
            sensorIds = sensorIds,
        )
}
