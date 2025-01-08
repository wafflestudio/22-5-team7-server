package com.toyProject7.karrot.profile.service

import com.toyProject7.karrot.manner.controller.Manner
import com.toyProject7.karrot.profile.ProfileNotFoundException
import com.toyProject7.karrot.profile.controller.Profile
import com.toyProject7.karrot.profile.persistence.ProfileRepository
import com.toyProject7.karrot.review.controller.Review
import com.toyProject7.karrot.review.persistence.ReviewRepository
import com.toyProject7.karrot.user.UserNotFoundException
import com.toyProject7.karrot.user.controller.User
import com.toyProject7.karrot.user.persistence.UserRepository
import org.springframework.stereotype.Service

@Service
class ProfileService(
    private val profileRepository: ProfileRepository,
    private val userRepository: UserRepository,
    private val reviewRepository: ReviewRepository,
) {
    fun getMyProfile(user: User): Profile {
        val profileEntity = profileRepository.findByUserId(user.id) ?: throw ProfileNotFoundException()
        return Profile.fromEntity(profileEntity)
    }

    fun getProfile(nickname: String): Profile {
        val userEntity = userRepository.findByNickname(nickname) ?: throw UserNotFoundException()
        val user = User.fromEntity(userEntity)
        val profileEntity = profileRepository.findByUserId(user.id) ?: throw ProfileNotFoundException()
        return Profile.fromEntity(profileEntity)
    }

    fun getManner(nickname: String): List<Manner> {
        val userEntity = userRepository.findByNickname(nickname) ?: throw UserNotFoundException()
        val user = User.fromEntity(userEntity)
        val profileEntity = profileRepository.findByUserId(user.id) ?: throw ProfileNotFoundException()
        return Profile.fromEntity(profileEntity).manners
    }

    fun getPreviousReviews(
        nickname: String,
        reviewId: Long,
    ): List<Review> {
        return reviewRepository.findTop10BySellerNicknameOrBuyerNicknameAndIdBeforeOrderByCreatedAtDesc(nickname, nickname, reviewId).map {
                reviewEntity ->
            Review.fromEntity(reviewEntity)
        }
    }
}
