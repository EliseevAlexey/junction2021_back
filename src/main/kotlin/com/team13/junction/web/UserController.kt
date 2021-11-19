package com.team13.junction.web

import com.team13.junction.config.EnableLogging
import com.team13.junction.model.UserDto
import com.team13.junction.model.toDto
import com.team13.junction.model.toDtos
import com.team13.junction.service.UserNotFoundException
import com.team13.junction.service.UserService
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestController
@RequestMapping("/users")
@EnableLogging
class UserController(private val service: UserService) {

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): UserDto? =
        service.get(id)
            ?.toDto()

    @PostMapping
    fun create(@RequestBody userDto: UserDto): UserDto =
        service.create(userDto)
            .toDto()

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody userDto: UserDto): UserDto? =
        service.update(id, userDto)
            ?.toDto()

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        service.delete(id)
    }

    @GetMapping
    fun getAll(): List<UserDto> =
        service.getAll()
            .toDtos()

}

@RestControllerAdvice
class UserExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    fun handleEntityNotFoundException(ex: UserNotFoundException): String =
        ex.message
}
