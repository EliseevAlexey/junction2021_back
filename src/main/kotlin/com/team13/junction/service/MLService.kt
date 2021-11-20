package com.team13.junction.service

import com.team13.junction.http.MlForecastRequest
import com.team13.junction.http.MlHttpClient
import org.springframework.stereotype.Service

@Service
class MLService(
    private val mlHttpClient: MlHttpClient
) {
    fun predictData(buildingId: Long, blockId: Long, sensorId: Long) {
        val request = MlForecastRequest(
            buildingId = buildingId,
            blockId = blockId,
            sensorId = sensorId,
        )

        mlHttpClient.forecast(request, 7)
        mlHttpClient.findAnomaly(request)
    }
}