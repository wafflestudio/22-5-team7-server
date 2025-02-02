package com.toyProject7.karrot.auction.persistence

import com.toyProject7.karrot.user.persistence.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface AuctionRepository : JpaRepository<AuctionEntity, Long> {
    fun findTop10ByIdBeforeAndStatusOrderByIdDesc(
        id: Long,
        status: Int,
    ): List<AuctionEntity>

    fun findTop10BySellerAndIdLessThanOrderByIdDesc(
        seller: UserEntity,
        id: Long,
    ): List<AuctionEntity>

    fun findTop10ByBidderAndIdLessThanOrderByIdDesc(
        bidder: UserEntity,
        id: Long,
    ): List<AuctionEntity>

    @Modifying
    @Query("UPDATE auctions a SET a.viewCount = a.viewCount + 1 WHERE a.id = :id")
    fun incrementViewCount(id: Long): Int

    fun countBySellerId(id: String): Int

    fun findByEndTimeBeforeAndStatus(
        endTime: Instant,
        status: Int,
    ): List<AuctionEntity>
}
