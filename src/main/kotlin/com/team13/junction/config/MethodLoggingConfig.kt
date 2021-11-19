package com.team13.junction.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.time.ExperimentalTime

@ExperimentalTime
@MethodLoggingEnabled
@Configuration
@EnableConfigurationProperties(MethodLoggingProps::class)
class MethodLoggingConfig(private val props: MethodLoggingProps) {

    @Bean
    fun methodLoggingAspect(): MethodLoggingAspect = MethodLoggingAspect(props)
}
