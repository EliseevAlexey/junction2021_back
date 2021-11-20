package com.team13.junction.web

import com.team13.junction.config.EnableLogging
import com.team13.junction.model.EventDto
import com.team13.junction.model.toDtos
import com.team13.junction.service.EventService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/events")
@EnableLogging
class EventController(private val service: EventService) {

    @GetMapping
    fun findAllToSent(): List<EventDto> =
        service.findAllToSent().toDtos()

}
