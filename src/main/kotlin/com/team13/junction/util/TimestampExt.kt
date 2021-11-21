package com.team13.junction.util

import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneOffset

fun LocalDateTime.toTimestamp(): Timestamp =
    Timestamp.from(this.toInstant(ZoneOffset.UTC))