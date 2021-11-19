package com.team13.junction.service

import com.team13.junction.dao.UserDao
import com.team13.junction.model.User
import com.team13.junction.model.UserDto
import com.team13.junction.model.UserRole
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(private val dao: UserDao) {

    fun get(id: Long): User? =
        dao.findByIdOrNull(id) ?: throw UserNotFoundException(id)

    fun create(userDto: UserDto): User =
        dao.save(
            User(
                name = userDto.name,
                role = UserRole.USER,
            )
        )

    @Transactional
    fun update(id: Long, userDto: UserDto): User? =
        get(id)?.let {
            it.name = userDto.name
            dao.save(it)
        }

    @Transactional
    fun delete(id: Long) {
        get(id)?.let { dao.delete(it) }
    }

    fun getAll(): List<User> =
        dao.findAll()

}

data class UserNotFoundException(override val message: String) : Exception(message) {
    constructor(id: Long) : this("User with ID #$id not found")
}
