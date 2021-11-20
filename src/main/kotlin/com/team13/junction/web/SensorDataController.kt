package com.team13.junction.web

import com.team13.junction.config.EnableLogging
import com.team13.junction.model.SensorData
import com.team13.junction.model.SensorDataJson
import com.team13.junction.service.SensorsDataService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sensors/")
@EnableLogging
class SensorDataController(private val service: SensorsDataService) {


    @PostMapping("/{sensorId}/data/water")
    fun createWaterSensorData(@PathVariable sensorId: Long, @RequestBody sensorDto: SensorDataJson): SensorData =
        service.create(sensorId, sensorDto)

}
