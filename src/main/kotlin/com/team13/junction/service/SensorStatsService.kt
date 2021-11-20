package com.team13.junction.service

import com.team13.junction.dao.ForecastDao
import com.team13.junction.model.Sensor
import com.team13.junction.model.SensorStats
import com.team13.junction.model.SensorType
import com.team13.junction.model.SensorType.DISHWASHER
import com.team13.junction.model.SensorType.MIXER
import com.team13.junction.model.SensorType.SHOWER
import com.team13.junction.model.SensorType.WASHER
import com.team13.junction.model.StatItem
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class SensorStatsService(private val forecastDao: ForecastDao) {

    fun getStats(sensor: Sensor, from: LocalDateTime, to: LocalDateTime) =
        SensorStats(
            data = getData(sensor, from, to),
            threshold = getThreshold(sensor.type),
        )

    private fun getData(sensor: Sensor, from: LocalDateTime, to: LocalDateTime): List<StatItem> {
        val sensorId = sensor.id
        val block = sensor.block
        val blockId = block.id
        val buildingId = block.building.id
        return forecastDao.findBy(
            start = from.toTimestamp(),
            to = to.toTimestamp(),
            buildingId = buildingId,
            blockId = blockId,
            sensorId = sensorId,
        ).map {
            StatItem(
                date = it.dateTime.toLocalDateTime(),
                value = it.value,
            )
        }
    }

    private fun LocalDateTime.toTimestamp() =
        Timestamp.from(this.toInstant(ZoneOffset.UTC))

    private fun getThreshold(type: SensorType): BigDecimal =
        when (type) {
            SHOWER -> 10.toBigDecimal()
            WASHER -> 10.toBigDecimal()
            DISHWASHER -> 10.toBigDecimal()
            MIXER -> 10.toBigDecimal()
        }

}
