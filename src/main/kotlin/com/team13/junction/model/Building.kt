package com.team13.junction.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "buildings")
class Building(
    var name: String,
    // todo add geolocation type
) {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long = 0
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
