package com.toyProject7.karrot.review.service

import com.toyProject7.karrot.profile.service.ProfileService
import com.toyProject7.karrot.review.ReviewContentLengthOutOfRangeException
import com.toyProject7.karrot.review.controller.Review
import com.toyProject7.karrot.review.controller.ReviewCreateRequest
import com.toyProject7.karrot.review.persistence.ReviewEntity
import com.toyProject7.karrot.review.persistence.ReviewRepository
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
    fun createReview(request: ReviewCreateRequest): Review {
        validateContent(request.content)

        val sellerEntity = userService.getUserEntityById(request.sellerId)
        val sellerProfileEntity = profileService.getProfileEntityByUserId(request.sellerId)

        val buyerEntity = userService.getUserEntityById(request.buyerId)
        val buyerProfileEntity = profileService.getProfileEntityByUserId(request.buyerId)

        val reviewEntity =
            ReviewEntity(
                seller = sellerEntity,
                buyer = buyerEntity,
                content = request.content,
                location = request.location,
                createdAt = Instant.now(),
                updatedAt = Instant.now(),
            ).let {
                reviewRepository.save(it)
            }

        if (request.isWritedByBuyer) {
            sellerProfileEntity.reviews += reviewEntity
        } else {
            buyerProfileEntity.reviews += reviewEntity
        }

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
