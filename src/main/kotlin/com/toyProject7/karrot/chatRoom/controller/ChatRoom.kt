package com.toyProject7.karrot.chatRoom.controller

import com.toyProject7.karrot.article.controller.Article
import com.toyProject7.karrot.chatRoom.persistence.ChatRoomEntity
import com.toyProject7.karrot.user.controller.User
import java.time.Instant

data class ChatRoom(
    val id: Long,
    val article: Article,
    val seller: User,
    val buyer: User,
    val chatMessage: String,
    val updatedAt: Instant,
) {
    companion object {
        fun fromEntity(
            entity: ChatRoomEntity,
            chatMessage: String,
        ): ChatRoom {
            return ChatRoom(
                id = entity.id!!,
                article = Article.fromEntity(entity.article),
                seller = User.fromEntity(entity.seller),
                buyer = User.fromEntity(entity.buyer),
                chatMessage = chatMessage,
                updatedAt = entity.updatedAt,
            )
        }
    }
}
