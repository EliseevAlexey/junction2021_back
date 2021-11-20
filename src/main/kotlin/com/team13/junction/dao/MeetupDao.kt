package com.team13.junction.dao

import com.team13.junction.model.Meetup
import org.springframework.data.jpa.repository.JpaRepository

interface MeetupDao : JpaRepository<Meetup, Long>
