package com.team13.junction.service

import com.team13.junction.dao.SensorDao
import com.team13.junction.model.Sensor
import com.team13.junction.model.SensorDto
import com.team13.junction.model.SensorStats
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class SensorService(
    private val dao: SensorDao,
    private val blockService: BlockService,
    private val sensorStatsService: SensorStatsService,
) {

    fun get(id: Long): SensorDto =
        getById(id)
            .toDto()

    fun getData(id: Long, from: LocalDateTime, to: LocalDateTime): SensorDto =
        getById(id)
            .toDto(from, to)

    private fun Sensor.toDto(from: LocalDateTime? = null, to: LocalDateTime? = null) =
        SensorDto(
            id = id,
            name = name,
            type = type,
            stats = if (from != null && to != null) getStats(this, from, to) else null
        )

    private fun getStats(sensor: Sensor, from: LocalDateTime, to: LocalDateTime): SensorStats =
        sensorStatsService.getStats(sensor, from, to)

    private fun getById(id: Long): Sensor =
        dao.findByIdOrNull(id) ?: throw EntityNotFound("Sensor with ID #$id not found")

    fun create(blockId: Long, dto: SensorDto): SensorDto {
        val block = blockService.get(blockId)

        return dao.save(
            Sensor(
                name = dto.name,
                type = dto.type,
                block = block,
            )
        ).toDto()
    }

    @Transactional
    fun update(id: Long, blockDto: SensorDto): SensorDto =
        getById(id).let {
            it.name = blockDto.name
            dao.save(it)
        }.toDto()

    @Transactional
    fun delete(id: Long) {
        getById(id).let { dao.delete(it) }
    }

    fun getAll(): List<SensorDto> =
        dao.findAll()
            .map { it.toDto() }

}

