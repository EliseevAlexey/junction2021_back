package com.team13.junction.service

import com.team13.junction.dao.EventDao
import com.team13.junction.model.Event
import com.team13.junction.model.EventDto
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EventService(
    private val dao: EventDao,
) {

    fun get(id: Long): Event =
        dao.findByIdOrNull(id) ?: throw EntityNotFound("Event with ID #$id not found")

    fun create(dto: EventDto): Event = dao.save(
        Event(data = dto.data)
    )

    @Transactional
    fun delete(id: Long) {
        get(id).let { dao.delete(it) }
    }

    @Transactional
    fun findAllToSent(): List<Event> {
        val events = dao.findAllToSent()

        return dao.saveAll(events.map { it.apply { isSent = true } })
    }

}
