package com.toyProject7.karrot.user.persistence

import com.toyProject7.karrot.user.controller.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, String> {
    fun findByUserId(userId: String): UserEntity?
    fun existsByUserId(userId: String): Boolean
    fun existsByNickname(nickname: String): Boolean
    fun findByEmail(email: String): User?
}