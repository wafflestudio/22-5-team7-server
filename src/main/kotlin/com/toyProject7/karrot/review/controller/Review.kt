package com.toyProject7.karrot.review.controller

import com.toyProject7.karrot.review.persistence.ReviewEntity
import com.toyProject7.karrot.user.controller.User
import java.time.Instant

data class Review(
    val id: Long,
    val content: String,
    val seller: User,
    val buyer: User,
    val location: String,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    companion object {
        fun fromEntity(entity: ReviewEntity): Review {
            return Review(
                id = entity.id!!,
                content = entity.content,
                seller = User.fromEntity(entity.seller),
                buyer = User.fromEntity(entity.buyer),
                location = entity.location,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
            )
        }
    }
}
