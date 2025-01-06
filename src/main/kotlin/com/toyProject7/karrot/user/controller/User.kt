package com.toyProject7.karrot.user.controller

import com.toyProject7.karrot.user.persistence.NormalUser
import com.toyProject7.karrot.user.persistence.SocialUser
import com.toyProject7.karrot.user.persistence.UserEntity

data class User(
    val id: String,
    val nickname: String,
    val location: String,
    val temperature: Double,
    val email: String,

    //Additional Attributes for NormalUser
    val userId: String? = null,
    val hashedPassword: String? = null,

    //Additional Attributes for SocialUser
    val provider: String? = null,
    val providerId: String? = null,
) {
    companion object {
        fun fromEntity(entity: UserEntity): User {
            return User(
                id = entity.id!!,
                nickname = entity.nickname,
                location = entity.location,
                temperature = entity.temperature,
                email = entity.email,

                userId = (entity as? NormalUser)?.userId,
                hashedPassword = (entity as? NormalUser)?.hashedPassword,

                provider = (entity as? SocialUser)?.provider,
                providerId = (entity as? SocialUser)?.providerId

            )
        }
    }
}
