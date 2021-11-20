package com.team13.junction.model

import org.locationtech.jts.geom.Geometry
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "meetups")
class Meetup(
    var name: String,
    var point: Geometry? = null,
    var startDate: LocalDateTime,
    var endDate: LocalDateTime,
    var cover: String,
    @Enumerated(EnumType.STRING)
    var type: MeetupType
) {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long = 0
}

data class MeetupDto(
    val id: Long? = null,
    val name: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val cover: String,
    val type: MeetupType,
    val point: Geometry?,
    val inProgress: Boolean
)

fun Meetup.toDto() =
    MeetupDto(
        id = id,
        name = name,
        point = point,
        startDate = startDate,
        endDate = endDate,
        cover = cover,
        type = type,
        inProgress = LocalDateTime.now().let { now -> now >= startDate && now <= endDate }
    )

fun List<Meetup>.toDtos() = map { it.toDto() }
