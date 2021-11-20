package com.team13.junction.service

import com.team13.junction.dao.BuildingDao
import com.team13.junction.model.Building
import com.team13.junction.model.BuildingDto
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BuildingService(private val dao: BuildingDao) {

    fun get(id: Long): Building =
        dao.findByIdOrNull(id) ?: throw EntityNotFound("Building with ID #$id not found")

    fun create(dto: BuildingDto) =
        dao.save(
            Building(
                name = dto.name
            )
        )

    @Transactional
    fun update(id: Long, dto: BuildingDto): Building =
        get(id).let {
            it.name = dto.name
            dao.save(it)
        }

    @Transactional
    fun delete(id: Long) {
        get(id).let { dao.delete(it) }
    }

    fun getAll(): List<Building> =
        dao.findAll()

}
