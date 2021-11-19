package com.team13.junction.dao

import com.team13.junction.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserDao : JpaRepository<User, Long>
