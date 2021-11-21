package com.team13.junction.web

import com.team13.junction.model.PushEventDto
import com.team13.junction.service.PushEventService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/push-events")
class PushEventController(private val service: PushEventService) {

    @GetMapping
    fun getEvent(): PushEventDto? =
        service.getEvent()

    @DeleteMapping
    fun disable() {
        service.disable()
    }

    @PutMapping
    fun enable() {
        service.enable()
    }

}
