package com.team13.junction.dao

import com.team13.junction.model.Meetup
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MeetupDao : JpaRepository<Meetup, Long> {
    @Query(
        value = "select * from meetups where tags && ARRAY [:tags]\\:\\:text[]",
        nativeQuery = true
    )
    fun findAllByTagsContains(tags: Array<String>): List<Meetup> //TODO

    @Query(
        value = "select unnest(tags) from meetups",
        nativeQuery = true
    )
    fun findAllTags(): List<String>
}
