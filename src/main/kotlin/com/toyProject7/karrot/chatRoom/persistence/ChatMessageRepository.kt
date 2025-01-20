package com.toyProject7.karrot.chatRoom.persistence

import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

interface ChatMessageRepository : JpaRepository<ChatMessageEntity, Long> {
    fun findTop10ByChatRoomIdAndCreatedAtBeforeOrderByCreatedAtDesc(
        chatRoomId: Long,
        createdAt: Instant,
    ): List<ChatMessageEntity>
}
