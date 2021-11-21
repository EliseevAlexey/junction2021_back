package com.team13.junction.service

import com.team13.junction.model.AssistantState
import com.team13.junction.model.ui.AssistantUI
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AssistantService(private val adviceService: AdviceService) {

    private var isWorking = false

    fun getStatus(): AssistantUI? =
        if (isWorking) {
            AssistantUI(
                state = AssistantState.STATE_5,
                message = adviceService.getAdvice()
            )
        } else null

    fun disable() {
        logger.warn("Disabling assistant")
        isWorking = false
    }

    fun enable() {
        logger.warn("Enabling assistant")
        isWorking = true
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AssistantService::class.java)
    }
}