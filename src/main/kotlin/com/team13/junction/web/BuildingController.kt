package com.team13.junction.web

import com.team13.junction.config.EnableLogging
import com.team13.junction.model.BlockDto
import com.team13.junction.model.BuildingDto
import com.team13.junction.model.toDto
import com.team13.junction.model.toDtos
import com.team13.junction.service.BlockService
import com.team13.junction.service.BuildingService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/buildings")
@EnableLogging
class BuildingController(
    private val service: BuildingService,
    private val blockService: BlockService
) {

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): BuildingDto =
        service.get(id)
            .toDto()

    @PostMapping
    fun create(@RequestBody buildingDto: BuildingDto): BuildingDto =
        service.create(buildingDto)
            .toDto()

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody buildingDto: BuildingDto): BuildingDto =
        service.update(id, buildingDto)
            .toDto()

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        service.delete(id)
    }

    @GetMapping
    fun getAll(): List<BuildingDto> =
        service.getAll()
            .toDtos()


    @PostMapping("/{buildingId}/blocks")
    fun createBlock(@PathVariable buildingId: Long, @RequestBody dto: BlockDto): BlockDto =
        blockService.create(buildingId, dto)
            .toDto()

}