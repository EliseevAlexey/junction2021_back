package com.team13.junction.web

import com.team13.junction.config.EnableLogging
import com.team13.junction.model.BlockDto
import com.team13.junction.model.SensorDto
import com.team13.junction.model.toDto
import com.team13.junction.model.toDtos
import com.team13.junction.service.BlockService
import com.team13.junction.service.SensorService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/blocks")
@EnableLogging
class BlockController(
    private val service: BlockService,
    private val sensorService: SensorService,
) {

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): BlockDto =
        service.get(id)
            .toDto()


    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody blockDto: BlockDto): BlockDto =
        service.update(id, blockDto)
            .toDto()

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        service.delete(id)
    }

    @GetMapping
    fun getAll(): List<BlockDto> =
        service.getAll()
            .toDtos()

    @PostMapping("/{blockId}/sensors")
    fun createSensor(@PathVariable blockId: Long, @RequestBody sensorDto: SensorDto): SensorDto =
        sensorService.create(blockId, sensorDto)
            .toDto()


}
