package com.team13.junction.web

import com.team13.junction.config.EnableLogging
import com.team13.junction.model.SensorDto
import com.team13.junction.model.ui.MainUiPage
import com.team13.junction.service.SensorService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/sensors")
@EnableLogging
class SensorController(private val service: SensorService) {

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): SensorDto =
        service.get(id)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody buildingDto: SensorDto): SensorDto =
        service.update(id, buildingDto)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        service.delete(id)
    }

    @GetMapping
    fun getAll(): List<SensorDto> =
        service.getAll()

    @GetMapping("/{id}/data")
    fun getAllData(
        @PathVariable id: Long,
        @RequestParam @DateTimeFormat(iso = DATE_TIME) from: LocalDateTime,
        @RequestParam @DateTimeFormat(iso = DATE_TIME) to: LocalDateTime,
    ): MainUiPage =
        service.getData(id, SensorUtil.ALL, from, to)

    @GetMapping("/{id}/data/water")
    fun getWaterData(
        @PathVariable id: Long,
        @RequestParam @DateTimeFormat(iso = DATE_TIME) from: LocalDateTime,
        @RequestParam @DateTimeFormat(iso = DATE_TIME) to: LocalDateTime,
    ): MainUiPage =
        service.getData(id, SensorUtil.WATER, from, to)

    @GetMapping("/{id}/data/energy")
    fun getEnergyData(
        @PathVariable id: Long,
        @RequestParam @DateTimeFormat(iso = DATE_TIME) from: LocalDateTime,
        @RequestParam @DateTimeFormat(iso = DATE_TIME) to: LocalDateTime,
    ): MainUiPage =
        service.getData(id, SensorUtil.ENERGY, from, to)
}
