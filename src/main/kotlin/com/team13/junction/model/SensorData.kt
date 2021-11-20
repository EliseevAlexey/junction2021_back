package com.team13.junction.model

import com.vladmihalcea.hibernate.type.json.JsonType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "sensors_data")
@TypeDefs(
    value = [
        TypeDef(name = "json", typeClass = JsonType::class)
    ]
)
class SensorData(
    @ManyToOne
    @JoinColumn(name = "building_id")
    val building: Building,
    @ManyToOne
    @JoinColumn(name = "block_id")
    val block: Block,
    @ManyToOne
    @JoinColumn(name = "sensor_id")
    val sensor: Sensor,
    @Enumerated(EnumType.STRING)
    @Column(name = "sensor_type")
    val type: SensorSubgroup,
    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    val data: SensorDataJson,
) {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long = 0

    val timestamp: LocalDateTime = LocalDateTime.now()
}