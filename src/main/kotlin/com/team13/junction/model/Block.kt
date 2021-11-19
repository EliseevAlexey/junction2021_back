package com.team13.junction.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "blocks")
class Block(
    var name: String,
) {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long = 0
}

data class BlockDto(
    val id: Long? = null,
    val name: String,
)

fun Block.toDto() =
    BlockDto(
        id = id,
        name = name,
    )

fun List<Block>.toDtos() = map { it.toDto() }
