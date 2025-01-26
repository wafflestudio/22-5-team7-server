package com.toyProject7.karrot.chatRoom.controller

import com.toyProject7.karrot.chatRoom.persistence.ChatMessageEntity
import java.time.Instant

data class ChatMessage(
    val chatRoomId: Long,
    val senderNickname: String,
    val content: String,
    val createdAt: Instant,
) {
    companion object {
        fun fromEntity(entity: ChatMessageEntity): ChatMessage {
            return ChatMessage(
                chatRoomId = entity.chatRoom.id!!,
                senderNickname = entity.sender.nickname,
                content = entity.content,
                createdAt = entity.createdAt,
            )
        }
    }
}
