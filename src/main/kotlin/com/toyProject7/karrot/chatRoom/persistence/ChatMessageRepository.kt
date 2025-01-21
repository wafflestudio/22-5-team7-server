package com.toyProject7.karrot.chatRoom.persistence

import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

interface ChatMessageRepository : JpaRepository<ChatMessageEntity, Long> {
    fun findTop30ByChatRoomIdAndCreatedAtBeforeOrderByCreatedAtDesc(
        chatRoomId: Long,
        createdAt: Instant,
    ): List<ChatMessageEntity>

    fun findTop1ByChatRoomIdOrderByCreatedAtDesc(chatRoomId: Long): ChatMessageEntity?
}
