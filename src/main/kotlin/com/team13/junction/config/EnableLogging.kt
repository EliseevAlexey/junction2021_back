package com.team13.junction.config

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class EnableLogging(
    val logEntry: Boolean = true,
    val logReturn: Boolean = true,
)
