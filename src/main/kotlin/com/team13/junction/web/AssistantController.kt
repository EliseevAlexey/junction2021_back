package com.team13.junction.web

import com.team13.junction.config.EnableLogging
import com.team13.junction.model.ui.AssistantUI
import com.team13.junction.service.AssistantService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/assistant")
class AssistantController(private val service: AssistantService) {

    @GetMapping
    fun getStatus(): AssistantUI? =
        service.getStatus()

    @DeleteMapping
    fun disable() {
        service.disable()
    }

    @PutMapping
    fun enable() {
        service.enable()
    }

}