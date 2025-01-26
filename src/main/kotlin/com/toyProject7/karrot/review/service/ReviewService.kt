package com.toyProject7.karrot.review.service

import com.toyProject7.karrot.profile.service.ProfileService
import com.toyProject7.karrot.review.ReviewContentLengthOutOfRangeException
import com.toyProject7.karrot.review.controller.Review
import com.toyProject7.karrot.review.persistence.ReviewEntity
import com.toyProject7.karrot.review.persistence.ReviewRepository
import com.toyProject7.karrot.user.controller.User
import com.toyProject7.karrot.user.service.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val userService: UserService,
    private val profileService: ProfileService,
) {
    @Transactional
    fun createReview(
        sellerNickname: String,
        buyerNickname: String,
        content: String,
        location: String,
    ): Review {
        validateContent(content)

        val sellerEntity = userService.getUserEntityByNickname(sellerNickname)
        val seller = User.fromEntity(sellerEntity)
        val sellerProfileEntity = profileService.getProfileEntityByUserId(seller.id)

        val buyerEntity = userService.getUserEntityByNickname(buyerNickname)
        val buyer = User.fromEntity(buyerEntity)
        val buyerProfileEntity = profileService.getProfileEntityByUserId(buyer.id)

        val reviewEntity =
            ReviewEntity(
                seller = sellerEntity,
                buyer = buyerEntity,
                content = content,
                location = location,
                createdAt = Instant.now(),
                updatedAt = Instant.now(),
            ).let {
                reviewRepository.save(it)
            }

        sellerProfileEntity.reviews += reviewEntity
        buyerProfileEntity.reviews += reviewEntity

        return Review.fromEntity(reviewEntity)
    }

    @Transactional
    fun getPreviousReviews(
        nickname: String,
        reviewId: Long,
    ): List<Review> {
        return reviewRepository.findTop10BySellerNicknameOrBuyerNicknameAndIdBeforeOrderByCreatedAtDesc(nickname, nickname, reviewId).map {
                reviewEntity ->
            Review.fromEntity(reviewEntity)
        }
    }

    private fun validateContent(content: String) {
        if (content.isBlank()) {
            throw ReviewContentLengthOutOfRangeException()
        }
        if (content.length > 100) {
            throw ReviewContentLengthOutOfRangeException()
        }
    }
}
