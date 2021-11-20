package com.team13.junction.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "events")
class Event(
    var data: String,
) {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long = 0

    @Column(name = "is_sent")
    var isSent: Boolean = false
}

data class EventDto(
    val id: Long? = null,
    val data: String,
)

fun Event.toDto() =
    EventDto(
        id = id,
        data = data,
    )

fun List<Event>.toDtos() = map { it.toDto() }
