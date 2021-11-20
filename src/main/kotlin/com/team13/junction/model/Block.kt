package com.team13.junction.model

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "blocks")
class Block(
    var name: String,
    @ManyToOne
    @JoinColumn(name = "building_id")
    var building: Building,
) {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long = 0

    @OneToMany(mappedBy = "block", cascade = [CascadeType.REMOVE])
    var sensors: List<Sensor> = emptyList()
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
