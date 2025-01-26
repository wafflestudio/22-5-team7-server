package com.toyProject7.karrot.auction.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface AuctionLikesRepository : JpaRepository<AuctionLikesEntity, String> {
    fun existsByUserIdAndAuctionId(
        userId: String,
        articleId: Long,
    ): Boolean

    @Query("SELECT al FROM auction_likes al WHERE al.user.id = :userId AND al.auction.id = :auctionId")
    fun findByUserIdAndArticleId(
        @Param("userId") userId: String,
        @Param("auctionId") auctionId: Long,
    ): AuctionLikesEntity?
}
