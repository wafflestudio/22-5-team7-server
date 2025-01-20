package com.toyProject7.karrot.chatRoom.service

import com.toyProject7.karrot.article.ArticleNotFoundException
import com.toyProject7.karrot.article.persistence.ArticleRepository
import com.toyProject7.karrot.chatRoom.ChatRoomNotFoundException
import com.toyProject7.karrot.chatRoom.ThisRoomIsNotYoursException
import com.toyProject7.karrot.chatRoom.controller.ChatMessage
import com.toyProject7.karrot.chatRoom.controller.ChatRoom
import com.toyProject7.karrot.chatRoom.persistence.ChatMessageEntity
import com.toyProject7.karrot.chatRoom.persistence.ChatMessageRepository
import com.toyProject7.karrot.chatRoom.persistence.ChatRoomEntity
import com.toyProject7.karrot.chatRoom.persistence.ChatRoomRepository
import com.toyProject7.karrot.user.UserNotFoundException
import com.toyProject7.karrot.user.controller.User
import com.toyProject7.karrot.user.persistence.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class ChatRoomService(
    private val chatMessageRepository: ChatMessageRepository,
    private val chatRoomRepository: ChatRoomRepository,
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository,
) {
    @Transactional
    fun saveMessage(chatMessage: ChatMessage): ChatMessage {
        val chatRoomEntity = chatRoomRepository.findById(chatMessage.chatRoomId).orElseThrow { ChatRoomNotFoundException() }
        val senderEntity = userRepository.findByNickname(chatMessage.senderNickname) ?: throw UserNotFoundException()
        val chatMessageEntity =
            ChatMessageEntity(
                chatRoom = chatRoomEntity,
                sender = senderEntity,
                content = chatMessage.content,
                createdAt = Instant.now(),
            )
        chatMessageRepository.save(chatMessageEntity)
        chatRoomEntity.updatedAt = Instant.now()
        return ChatMessage.fromEntity(chatMessageEntity)
    }

    @Transactional
    fun getChatRooms(
        user: User,
        updatedAt: Instant,
    ): List<ChatRoom> {
        val chatRoomEntities: List<ChatRoomEntity> =
            chatRoomRepository.findTop10BySellerIdOrBuyerIdAndUpdatedAtBeforeOrderByUpdatedAtDesc(
                sellerId = user.id,
                buyerId = user.id,
                updatedAt = updatedAt,
            )
        return chatRoomEntities.map { chatRoomEntity -> ChatRoom.fromEntity(chatRoomEntity) }
    }

    @Transactional
    fun getChatRoom(
        chatRoomId: Long,
        user: User,
        createdAt: Instant,
    ): List<ChatMessage> {
        val chatRoomEntity = chatRoomRepository.findById(chatRoomId).orElseThrow { ChatRoomNotFoundException() }
        val chatRoom = ChatRoom.fromEntity(chatRoomEntity)
        if (chatRoom.buyer != user && chatRoom.seller != user) throw ThisRoomIsNotYoursException()

        val chatMessageEntities: List<ChatMessageEntity> =
            chatMessageRepository.findTop10ByChatRoomIdAndCreatedAtBeforeOrderByCreatedAtDesc(
                chatRoomId = chatRoomId,
                createdAt = createdAt,
            )
        return chatMessageEntities.map { chatMessageEntity -> ChatMessage.fromEntity(chatMessageEntity) }
    }

    @Transactional
    fun createChatRoom(
        articleId: Long,
        sellerId: String,
        buyerId: String,
    ): ChatRoom {
        val articleEntity = articleRepository.findById(articleId).orElseThrow { ArticleNotFoundException() }
        val sellerEntity = userRepository.findById(sellerId).orElseThrow { UserNotFoundException() }
        val buyerEntity = userRepository.findById(buyerId).orElseThrow { UserNotFoundException() }
        val chatRoomEntity =
            ChatRoomEntity(
                article = articleEntity,
                seller = sellerEntity,
                buyer = buyerEntity,
                updatedAt = Instant.now(),
            )
        chatRoomRepository.save(chatRoomEntity)
        return ChatRoom.fromEntity(chatRoomEntity)
    }
}
