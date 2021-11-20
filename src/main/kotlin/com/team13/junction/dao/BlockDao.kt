package com.team13.junction.dao

import com.team13.junction.model.Block
import org.springframework.data.jpa.repository.JpaRepository

interface BlockDao : JpaRepository<Block, Long>
