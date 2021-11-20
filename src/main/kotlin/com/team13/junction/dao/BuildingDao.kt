package com.team13.junction.dao

import com.team13.junction.model.Building
import org.springframework.data.jpa.repository.JpaRepository

interface BuildingDao : JpaRepository<Building, Long>
