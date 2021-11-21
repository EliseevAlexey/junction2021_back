package com.team13.junction.service

import com.team13.junction.model.PushEventDto
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class PushEventService(private val adviceService: AdviceService) {

    private var isWorking = false

    fun getEvent(): PushEventDto? =
        if (isWorking) {
            PushEventDto(
                title = "Advice of a day",
                message = adviceService.getAdvice()
            )
        } else null

    fun disable() {
        logger.warn("Disabling push-events")
        isWorking = false
    }

    fun enable() {
        logger.warn("Enabling push-events")
        isWorking = true
    }

    companion object {
        private val logger = LoggerFactory.getLogger(PushEventService::class.java)
    }

}
