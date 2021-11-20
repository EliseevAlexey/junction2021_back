package com.team13.junction.model

import org.locationtech.jts.geom.Geometry
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "buildings")
class Building(
    var name: String,
    var point: Geometry? = null,
) {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long = 0

    @OneToMany(mappedBy="building", cascade = [CascadeType.REMOVE])
    var blocks: List<Block> = emptyList()
}

data class BuildingDto(
    val id: Long? = null,
    val name: String,
)

fun Building.toDto() =
    BuildingDto(
        id = id,
        name = name,
    )

fun List<Building>.toDtos() = map { it.toDto() }
