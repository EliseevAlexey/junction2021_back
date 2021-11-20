package com.team13.junction.service

data class EntityNotFound(override val message: String) : Exception(message)
