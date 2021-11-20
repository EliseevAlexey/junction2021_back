package com.team13.junction.service

import com.team13.junction.dao.BlockDao
import com.team13.junction.model.Block
import com.team13.junction.model.BlockDto
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BlockService(private val dao: BlockDao, private val buildingService: BuildingService) {

    fun get(id: Long): Block =
        dao.findByIdOrNull(id) ?: throw EntityNotFound("Block with ID #$id not found")

    fun create(buildingId: Long, dto: BlockDto): Block {
        val building = buildingService.get(buildingId)

        return dao.save(
            Block(
                name = dto.name,
                building = building,
            )
        )
    }

    @Transactional
    fun update(id: Long, blockDto: BlockDto): Block =
        get(id).let {
            it.name = blockDto.name
            dao.save(it)
        }

    @Transactional
    fun delete(id: Long) {
        get(id).let { dao.delete(it) }
    }

    fun getAll(): List<Block> =
        dao.findAll()

}
