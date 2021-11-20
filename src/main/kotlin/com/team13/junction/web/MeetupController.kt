package com.team13.junction.web

import com.team13.junction.config.EnableLogging
import com.team13.junction.model.MeetupDto
import com.team13.junction.model.toDto
import com.team13.junction.model.toDtos
import com.team13.junction.service.MeetupService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/meetups")
@EnableLogging
class MeetupController(
    private val service: MeetupService,
) {

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): MeetupDto =
        service.get(id)
            .toDto()

    @PostMapping
    fun create(@RequestBody userDto: MeetupDto): MeetupDto =
        service.create(userDto)
            .toDto()

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody blockDto: MeetupDto): MeetupDto =
        service.update(id, blockDto)
            .toDto()

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        service.delete(id)
    }

    @GetMapping
    fun getAll(): List<MeetupDto> =
        service.getAll()
            .toDtos()

}
