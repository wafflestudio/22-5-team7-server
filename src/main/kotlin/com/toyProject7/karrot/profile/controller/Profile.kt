package com.toyProject7.karrot.profile.controller

import com.toyProject7.karrot.manner.controller.Manner
import com.toyProject7.karrot.profile.persistence.ProfileEntity
import com.toyProject7.karrot.review.controller.Review
import com.toyProject7.karrot.user.controller.User

data class Profile(
    val id: Long,
    val user: User,
    val itemCount: Int,
    val manners: List<Manner>,
    val reviews: List<Review>,
    val reviewCount: Int,
) {
    companion object {
        fun fromEntity(
            entity: ProfileEntity,
            itemCount: Int,
        ): Profile {
            return Profile(
                id = entity.id!!,
                user = User.fromEntity(entity.user),
                itemCount = itemCount,
                manners = entity.manners.map { mannerEntity -> Manner.fromEntity(mannerEntity) },
                reviews = entity.reviews.map { reviewEntity -> Review.fromEntity(reviewEntity) },
                reviewCount = entity.reviews.size,
            )
        }
    }
}
