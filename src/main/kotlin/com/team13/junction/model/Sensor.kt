package com.team13.junction.model

import com.vladmihalcea.hibernate.type.json.JsonType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "sensors")
@TypeDefs(
    value = [
        TypeDef(name = "json", typeClass = JsonType::class)
    ]
)
class Sensor(
    var name: String,
    @Enumerated(EnumType.STRING)
    var type: SensorType,
    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    var data: Array<SensorData>,
) {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long = 0
}

data class SensorDto(
    val id: Long? = null,
    val name: String,
)

fun Sensor.toDto() =
    SensorDto(
        id = id,
        name = name,
    )

fun List<Sensor>.toDtos() = map { it.toDto() }
