package com.toyProject7.karrot.review.service

import com.toyProject7.karrot.review.ReviewContentLengthOutOfRangeException
import com.toyProject7.karrot.review.UserNotFoundException
import com.toyProject7.karrot.review.controller.Review
import com.toyProject7.karrot.review.persistence.ReviewEntity
import com.toyProject7.karrot.review.persistence.ReviewRepository
import com.toyProject7.karrot.user.persistence.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserRepository,
) {
    @Transactional
    fun createReview(
        sellerNickname: String,
        buyerNickname: String,
        content: String,
        location: String,
    ): Review {
        validateContent(content)

        val sellerEntity = userRepository.findByNickname(sellerNickname) ?: throw UserNotFoundException()
        val buyerEntity = userRepository.findByNickname(buyerNickname) ?: throw UserNotFoundException()
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
        return Review.fromEntity(reviewEntity)
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
