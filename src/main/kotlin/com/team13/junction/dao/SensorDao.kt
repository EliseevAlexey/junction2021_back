package com.team13.junction.dao

import com.team13.junction.model.Sensor
import org.springframework.data.jpa.repository.JpaRepository

interface SensorDao : JpaRepository<Sensor, Long>
