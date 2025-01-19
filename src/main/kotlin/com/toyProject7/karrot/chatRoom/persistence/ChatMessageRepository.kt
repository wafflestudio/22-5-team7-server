package com.toyProject7.karrot.chatRoom.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface ChatMessageRepository : JpaRepository<ChatMessageEntity, Long> {
    fun findAllByChatRoomIdOrderByCreatedAtAsc(chatRoomId: Long): List<ChatMessageEntity>
}
