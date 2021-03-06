package com.team13.junction.model

import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name = "users")
class User(
    var name: String,
    @Enumerated(EnumType.STRING) var role: UserRole,
) {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long = 0
}

data class UserDto(
    val id: Long? = null,
    val name: String,
    val role: UserRole,
)

fun User.toDto() =
    UserDto(
        id = id,
        name = name,
        role = role,
    )

fun List<User>.toDtos() = map { it.toDto() }
