package com.team13.junction.model

import java.sql.Timestamp
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name = "anomaly")
class Anomaly(
    @Id val id: Long,
    val timestamp: Timestamp,
    val sensorId: Long,
    val buildingId: Long,
    val blockId: Long,
)
