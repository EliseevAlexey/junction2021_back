package com.team13.junction.service

import com.team13.junction.dao.SensorDao
import com.team13.junction.model.Sensor
import com.team13.junction.model.SensorDto
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SensorService(
    private val dao: SensorDao,
    private val blockService: BlockService,
) {

    fun get(id: Long): Sensor =
        dao.findByIdOrNull(id) ?: throw EntityNotFound("Sensor with ID #$id not found")

    fun create(blockId: Long, dto: SensorDto): Sensor {
        val block = blockService.get(blockId)

        return dao.save(
            Sensor(
                name = dto.name,
                type = dto.type,
                block = block,
            )
        )
    }

    @Transactional
    fun update(id: Long, blockDto: SensorDto): Sensor =
        get(id).let {
            it.name = blockDto.name
            dao.save(it)
        }

    @Transactional
    fun delete(id: Long) {
        get(id).let { dao.delete(it) }
    }

    fun getAll(): List<Sensor> =
        dao.findAll()

}

