package com.team13.junction.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.PositiveOrZero

@ConfigurationProperties("method-logging")
@Validated
data class MethodLoggingProps(
    @PositiveOrZero var objectSizeLimit: Int = 1000,
    @PositiveOrZero var collectionSizeLimit: Int = 10
)
