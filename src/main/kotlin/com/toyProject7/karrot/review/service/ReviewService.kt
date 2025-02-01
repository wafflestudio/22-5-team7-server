package com.toyProject7.karrot.review.service

import com.toyProject7.karrot.article.ArticleIsNotEndException
import com.toyProject7.karrot.article.YouAreNotBuyerOrSellerException
import com.toyProject7.karrot.article.service.ArticleService
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
    private val articleService: ArticleService,
) {
    @Transactional
    fun createReview(request: ReviewCreateRequest): Review {
        validateContent(request.content)

        val sellerEntity = userService.getUserEntityById(request.sellerId)
        val sellerProfileEntity = profileService.getProfileEntityByUserId(request.sellerId)

        val buyerEntity = userService.getUserEntityById(request.buyerId)
        val buyerProfileEntity = profileService.getProfileEntityByUserId(request.buyerId)

        val articleEntity = articleService.getArticleEntityById(request.articleId)
        if (articleEntity.status != 2) {
            throw ArticleIsNotEndException()
        }
        if (articleEntity.buyer != sellerEntity && articleEntity.buyer != buyerEntity) {
            throw YouAreNotBuyerOrSellerException()
        }

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

    private fun validateContent(content: String) {
        if (content.isBlank()) {
            throw ReviewContentLengthOutOfRangeException()
        }
        if (content.length > 100) {
            throw ReviewContentLengthOutOfRangeException()
        }
    }
}
