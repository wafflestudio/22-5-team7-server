package com.toyProject7.karrot.user.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface NormalUserRepository : JpaRepository<NormalUser, Long> {
    fun existsByUserId(userId: String): Boolean
    fun existsByNickname(nickname: String): Boolean
}