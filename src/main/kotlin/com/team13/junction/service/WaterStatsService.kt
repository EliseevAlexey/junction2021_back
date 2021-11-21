package com.team13.junction.service

import com.team13.junction.config.EnableLogging
import com.team13.junction.dao.WaterForecastDao
import com.team13.junction.model.ChartData
import com.team13.junction.model.Sensor
import com.team13.junction.model.ui.Chart
import com.team13.junction.model.ui.ChartItem
import com.team13.junction.service.ThresholdService.getThreshold
import com.team13.junction.util.toTimestamp
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
@EnableLogging
class WaterStatsService(
    private val waterForecastDao: WaterForecastDao,
    @Lazy private val sensorsDataService: SensorsDataService,
) {

    fun getStats(sensor: Sensor, from: LocalDateTime, to: LocalDateTime) =
        Chart(
            data = getData(sensor, from, to),
            threshold = getThreshold(sensor.sensorSubgroup),
        )

    fun getStats(
        sensorIds: List<Long>,
        from: LocalDateTime,
        to: LocalDateTime
    ): List<ChartData> {

        val now = getTo(to)
        val history = sensorsDataService.findByDateBetweenAndSensorIdIn(
            from = from,
            to = now,
            sensorIds = sensorIds
        ).map {
            ChartData(
                sensorId = it.sensor.id,
                blockId = it.block.id,
                date = it.timestamp,
                value = it.data.consumption,
            )
        }

        val forecast = waterForecastDao.findByDateBetweenAndSensorIdIn(
            from = now.toTimestamp(),
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



        return history.limitPerDay(2) + forecast.limitPerDay(2)
    }

    private fun List<ChartData>.limitPerDay(limit: Int) =
        groupBy { it.date.toLocalDate() }
            .flatMap { (_, list) ->
                list.take(limit)
            }

    private fun getTo(to: LocalDateTime): LocalDateTime {
        val now = LocalDateTime.now()
        return if (now >= to) to else now
    }

    private fun getData(sensor: Sensor, from: LocalDateTime, to: LocalDateTime): List<ChartItem> =
        findBy(
            from = from,
            to = to,
            sensorId = sensor.id
        )

    private fun findBy(
        sensorId: Long,
        from: LocalDateTime,
        to: LocalDateTime,
    ): List<ChartItem> =
        waterForecastDao.findBy(
            from = from.toTimestamp(),
            to = to.toTimestamp(),
            sensorId = sensorId,
        ).map {
            ChartItem(
                date = it.date.toLocalDateTime(),
                value = it.waterNeutral,
            )
        }

}
