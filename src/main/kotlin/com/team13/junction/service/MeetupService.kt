package com.team13.junction.service

import com.team13.junction.dao.MeetupDao
import com.team13.junction.model.Meetup
import com.team13.junction.model.MeetupDto
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MeetupService(
    private val dao: MeetupDao,
) {

    fun get(id: Long): Meetup =
        dao.findByIdOrNull(id) ?: throw EntityNotFound("Meetup with ID #$id not found")

    fun create(dto: MeetupDto) =
        dao.save(
            Meetup(
                name = dto.name,
                startDate = dto.startDate,
                endDate = dto.endDate,
                cover = dto.cover,
                type = dto.type,
                tags = dto.tags.toTypedArray(),
                description = dto.description
            )
        )

    @Transactional
    fun update(id: Long, dto: MeetupDto): Meetup =
        get(id).let {
            it.name = dto.name
            it.startDate = dto.startDate
            it.endDate = dto.endDate
            it.cover = dto.cover
            it.type = dto.type
            it.description = dto.description
            it.tags = dto.tags.toTypedArray()
            dao.save(it)
        }

    @Transactional
    fun delete(id: Long) {
        get(id).let { dao.delete(it) }
    }

    fun getAll(tags: Array<String>): List<Meetup> = if (tags.isEmpty())
        dao.findAll()
    else
        dao.findAllByTagsContains(tags)

    fun getAllTags(): List<String> = dao.findAllTags()

}
