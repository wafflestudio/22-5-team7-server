package com.toyProject7.karrot.user.controller

import com.toyProject7.karrot.user.persistence.UserEntity

data class User(
    val id: String,
    val nickname: String,
    val userId: String,
    val location: String,
    val temperature: Double,
    val email: String,
) {
    companion object {
        fun fromEntity(entity: UserEntity): User {
            return User(
                id = entity.id!!,
                nickname = entity.nickname,
                userId = entity.userId,
                location = entity.location,
                temperature = entity.temperature,
                email = entity.email,
            )
        }
    }
}
