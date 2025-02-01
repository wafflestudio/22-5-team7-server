package com.toyProject7.karrot.chatRoom.persistence

import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

interface ChatRoomRepository : JpaRepository<ChatRoomEntity, Long> {
    fun findTop10BySellerIdOrBuyerIdAndUpdatedAtBeforeOrderByUpdatedAtDesc(
        sellerId: String,
        buyerId: String,
        updatedAt: Instant,
    ): List<ChatRoomEntity>

    fun findAllByArticleId(articleId: Long): List<ChatRoomEntity>

    fun existsByArticleIdAndBuyerId(
        articleId: Long,
        buyerId: String,
    ): Boolean

    fun findByArticleIdAndBuyerId(
        articleId: Long,
        buyerId: String,
    ): ChatRoomEntity
}
