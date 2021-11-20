package com.team13.junction

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class JunctionApplication

fun main(args: Array<String>) {
    runApplication<JunctionApplication>(*args)
}
