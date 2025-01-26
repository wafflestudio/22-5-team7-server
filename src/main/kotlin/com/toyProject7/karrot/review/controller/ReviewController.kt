package com.toyProject7.karrot.review.controller

import com.toyProject7.karrot.review.service.ReviewService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ReviewController(
    private val reviewService: ReviewService,
) {
    @PostMapping("/api/review/post")
    fun createReview(
        @RequestBody request: ReviewCreateRequest,
    ): ResponseEntity<ReviewCreateResponse> {
        val review = reviewService.createReview(request)
        return ResponseEntity.status(201).body(review)
    }
}

data class ReviewCreateRequest(
    val content: String,
    val location: String,
    val isWritedByBuyer: Boolean,
    val sellerId: String,
    val buyerId: String,
)

typealias ReviewCreateResponse = Review
