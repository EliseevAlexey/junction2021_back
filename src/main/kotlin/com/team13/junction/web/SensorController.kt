package com.team13.junction.web

import com.team13.junction.config.EnableLogging
import com.team13.junction.model.SensorDto
import com.team13.junction.model.toDto
import com.team13.junction.model.toDtos
import com.team13.junction.service.SensorService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sensors")
@EnableLogging
class SensorController(private val service: SensorService) {

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): SensorDto =
        service.get(id)
            .toDto()

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody buildingDto: SensorDto): SensorDto =
        service.update(id, buildingDto)
            .toDto()

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        service.delete(id)
    }

    @GetMapping
    fun getAll(): List<SensorDto> =
        service.getAll()
            .toDtos()

}
