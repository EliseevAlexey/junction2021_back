package com.team13.junction.service

import com.team13.junction.dao.WaterForecastDao
import com.team13.junction.model.Sensor
import com.team13.junction.model.ui.Chart
import com.team13.junction.model.ui.ChartData
import com.team13.junction.model.ui.ChartItem
import com.team13.junction.service.ThresholdService.getThreshold
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class WaterStatsService(private val waterForecastDao: WaterForecastDao) {

    fun getStats(sensor: Sensor, from: LocalDateTime, to: LocalDateTime) =
        Chart(
            data = getData(sensor, from, to),
            threshold = getThreshold(sensor.sensorSubgroup),
        )

    fun getStats(
        buildingId: Long,
        blockId: Long,
        sensorId: Long,
        from: LocalDateTime,
        to: LocalDateTime,
    ): List<ChartItem> {
        return findBy(
            from = from,
            to = to,
            buildingId = buildingId,
            blockId = blockId,
            sensorId = sensorId
        )
    }

    fun getStats(
        sensorIds: List<Long>,
        from: LocalDateTime,
        to: LocalDateTime
    ): List<ChartData> =
        waterForecastDao.findByDateBetweenAndSensorIdIn(
            from = from.toTimestamp(),
            to = to.toTimestamp(),
            sensorIds = sensorIds
        ).map {
            ChartData(
                sensorId = it.sensorId,
                blockId = it.blockId,
                date = it.date.toLocalDateTime(),
                value = it.waterNeutral,
            )
        }

    private fun getData(sensor: Sensor, from: LocalDateTime, to: LocalDateTime): List<ChartItem> =
        findBy(
            from = from,
            to = to,
            buildingId = sensor.block.building.id,
            blockId = sensor.block.id,
            sensorId = sensor.id
        )

    private fun findBy(
        buildingId: Long,
        blockId: Long,
        sensorId: Long,
        from: LocalDateTime,
        to: LocalDateTime,
    ): List<ChartItem> =
        waterForecastDao.findBy(
            from = from.toTimestamp(),
            to = to.toTimestamp(),
            buildingId = buildingId,
            blockId = blockId,
            sensorId = sensorId,
        ).map {
            ChartItem(
                date = it.date.toLocalDateTime(),
                value = it.waterNeutral,
            )
        }

    private fun LocalDateTime.toTimestamp() =
        Timestamp.from(this.toInstant(ZoneOffset.UTC))

}
