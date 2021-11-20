package com.team13.junction.model

import com.team13.junction.model.ui.Chart
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
@Table(name = "sensors")
class Sensor(
    var name: String,
    @Enumerated(EnumType.STRING)
    @Column(name = "type") var sensorSubgroup: SensorSubgroup,
    @ManyToOne
    @JoinColumn(name = "block_id")
    val block: Block,
) {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long = 0
}

data class SensorDto(
    val id: Long? = null,
    val type: SensorSubgroup,
    val name: String,
    val stats: Chart?,
)
