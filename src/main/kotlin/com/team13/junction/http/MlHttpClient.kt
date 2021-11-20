package com.team13.junction.http

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@FeignClient(name="ml", url = "http://localhost:5000/")
interface MlHttpClient {

    @RequestMapping(method = [RequestMethod.POST], value = ["/forecast/{period}"])
    fun forecast(request: MlForecastRequest, @PathVariable("period") period: Int)

    @RequestMapping(method = [RequestMethod.POST], value = ["/anomaly"])
    fun findAnomaly(request: MlForecastRequest)
}

data class MlForecastRequest(
    @JsonProperty("building_id")
    val buildingId: Long,
    @JsonProperty("block_id")
    val blockId: Long,
    @JsonProperty("sensor_id")
    val sensorId: Long,
)