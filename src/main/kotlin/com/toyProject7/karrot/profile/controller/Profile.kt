package com.toyProject7.karrot.profile.controller

import com.toyProject7.karrot.manner.controller.Manner
import com.toyProject7.karrot.profile.persistence.ProfileEntity
import com.toyProject7.karrot.review.controller.Review
import com.toyProject7.karrot.user.controller.User

data class Profile(
    val id: Long,
    val user: User,
    val manners: List<Manner>,
    val reviews: List<Review>,
    val mannerCount: Int,
    val reviewCount: Int,
) {
    companion object {
        fun fromEntity(entity: ProfileEntity): Profile {
            return Profile(
                id = entity.id!!,
                user = User.fromEntity(entity.user),
                manners = entity.manners.map { mannerEntity -> Manner.fromEntity(mannerEntity) },
                reviews = entity.reviews.map { reviewEntity -> Review.fromEntity(reviewEntity) },
                mannerCount = entity.manners.size,
                reviewCount = entity.reviews.size,
            )
        }
    }
}
