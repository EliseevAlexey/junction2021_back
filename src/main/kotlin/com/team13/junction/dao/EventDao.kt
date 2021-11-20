package com.team13.junction.dao

import com.team13.junction.model.Event
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface EventDao : JpaRepository<Event, Long> {
    @Query(
        value = "select * from events where is_sent = false limit :limit",
        nativeQuery = true
    )
    fun findAllToSent(limit: Int = 5): List<Event>
}
