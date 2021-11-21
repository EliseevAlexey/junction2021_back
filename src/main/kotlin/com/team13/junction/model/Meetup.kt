package com.team13.junction.model

import com.team13.junction.model.ui.PointDto
import com.team13.junction.model.ui.toPointDto
import com.vladmihalcea.hibernate.type.array.StringArrayType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
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
@TypeDefs(
    value = [
        TypeDef(name = "string-array", typeClass = StringArrayType::class)
    ]
)
class Meetup(
    var name: String,
    var point: Geometry? = null,
    var startDate: LocalDateTime,
    var endDate: LocalDateTime,
    var description: String,
    var cover: String,
    @Type(type = "string-array")
    var tags: Array<String>,
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
    val description: String,
    val type: MeetupType,
    val point: PointDto?,
    val tags: List<String>,
    val inProgress: Boolean
)

fun Meetup.toDto() =
    MeetupDto(
        id = id,
        name = name,
        point = point?.toPointDto(),
        startDate = startDate,
        endDate = endDate,
        cover = cover,
        type = type,
        inProgress = LocalDateTime.now().let { now -> now >= startDate && now <= endDate },
        tags = tags.toList(),
        description = description
    )

fun List<Meetup>.toDtos() = map { it.toDto() }
