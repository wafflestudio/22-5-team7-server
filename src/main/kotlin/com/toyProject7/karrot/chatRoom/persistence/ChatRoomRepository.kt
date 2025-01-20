package com.toyProject7.karrot.chatRoom.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface ChatRoomRepository : JpaRepository<ChatRoomEntity, Long> {
    fun findAllBySellerIdOrBuyerIdOrderByUpdatedAtDesc(
        sellerId: String,
        buyerId: String,
    ): List<ChatRoomEntity>
}
