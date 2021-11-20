package com.team13.junction.dao

import com.team13.junction.model.SensorData
import org.springframework.data.jpa.repository.JpaRepository

interface SensorDataDao : JpaRepository<SensorData, Long>
