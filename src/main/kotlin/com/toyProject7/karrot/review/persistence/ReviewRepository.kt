package com.toyProject7.karrot.review.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface ReviewRepository : JpaRepository<ReviewEntity, String> {
    fun findTop10BySellerIdOrBuyerIdAndIdBeforeOrderByCreatedAtDesc(
        sellerId: String,
        buyerId: String,
        id: Long,
    ): List<ReviewEntity>
}
