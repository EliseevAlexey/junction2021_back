package com.team13.junction.web

import com.team13.junction.service.EntityNotFound
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleEntityNotFoundException(ex: EntityNotFound): String =
        ex.message
}
