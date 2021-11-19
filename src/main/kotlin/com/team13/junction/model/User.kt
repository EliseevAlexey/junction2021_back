package com.team13.junction.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "users")
class User(
    var name: String?,
) {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long = 0
}

data class UserDto(
    val id: Long? = null,
    val name: String?,
)

fun User.toDto() =
    UserDto(
        id = id,
        name = name,
    )

fun List<User>.toDtos() = map { it.toDto() }
