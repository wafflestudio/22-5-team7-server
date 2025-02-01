package com.toyProject7.karrot.chatRoom.service

import com.toyProject7.karrot.article.service.ArticleService
import com.toyProject7.karrot.chatRoom.ChatRoomNotFoundException
import com.toyProject7.karrot.chatRoom.SellerCreateChatRoomWithSellerException
import com.toyProject7.karrot.chatRoom.ThisRoomIsNotYoursException
import com.toyProject7.karrot.chatRoom.controller.ChatMessage
import com.toyProject7.karrot.chatRoom.controller.ChatRoom
import com.toyProject7.karrot.chatRoom.controller.ChatRoomResponse
import com.toyProject7.karrot.chatRoom.persistence.ChatMessageEntity
import com.toyProject7.karrot.chatRoom.persistence.ChatMessageRepository
import com.toyProject7.karrot.chatRoom.persistence.ChatRoomEntity
import com.toyProject7.karrot.chatRoom.persistence.ChatRoomRepository
import com.toyProject7.karrot.user.controller.User
import com.toyProject7.karrot.user.service.UserService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class ChatRoomService(
    private val chatMessageRepository: ChatMessageRepository,
    private val chatRoomRepository: ChatRoomRepository,
    private val userService: UserService,
    private val articleService: ArticleService,
) {
    @Transactional
    fun saveMessage(chatMessage: ChatMessage): ChatMessage {
        val chatRoomEntity = chatRoomRepository.findByIdOrNull(chatMessage.chatRoomId) ?: throw ChatRoomNotFoundException()
        val senderEntity = userService.getUserEntityByNickname(chatMessage.senderNickname)
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
        return chatRoomEntities.map { chatRoomEntity ->
            val latestChatMessage = chatMessageRepository.findTop1ByChatRoomIdOrderByCreatedAtDesc(chatRoomEntity.id!!)?.content ?: ""
            ChatRoom.fromEntity(chatRoomEntity, latestChatMessage)
        }
    }

    @Transactional
    fun getChatRoom(
        chatRoomId: Long,
        user: User,
        createdAt: Instant,
    ): ChatRoomResponse {
        val chatRoomEntity = chatRoomRepository.findById(chatRoomId).orElseThrow { ChatRoomNotFoundException() }
        val chatRoom = ChatRoom.fromEntity(chatRoomEntity, "")
        if (chatRoom.buyer != user && chatRoom.seller != user) throw ThisRoomIsNotYoursException()

        val chatMessageEntities: List<ChatMessageEntity> =
            chatMessageRepository.findTop30ByChatRoomIdAndCreatedAtBeforeOrderByCreatedAtDesc(
                chatRoomId = chatRoomId,
                createdAt = createdAt,
            )
        val messages = chatMessageEntities.map { chatMessageEntity -> ChatMessage.fromEntity(chatMessageEntity) }
        return ChatRoomResponse(
            chatRoom = chatRoom,
            messages = messages,
        )
    }

    @Transactional
    fun createChatRoom(
        user: User,
        articleId: Long,
        sellerId: String,
        buyerId: String,
    ): ChatRoom {
        if (user.id == sellerId) {
            throw SellerCreateChatRoomWithSellerException()
        }
        val articleEntity = articleService.getArticleEntityById(articleId)
        val sellerEntity = userService.getUserEntityById(sellerId)
        val buyerEntity = userService.getUserEntityById(buyerId)
        val chatRoomEntity =
            ChatRoomEntity(
                article = articleEntity,
                seller = sellerEntity,
                buyer = buyerEntity,
                updatedAt = Instant.now(),
            )
        chatRoomRepository.save(chatRoomEntity)
        return ChatRoom.fromEntity(chatRoomEntity, "")
    }
    @Transactional
    fun createChatRoom(
        articleId: Long,
        sellerId: String,
        buyerId: String,
    ): ChatRoom {
        val articleEntity = articleService.getArticleEntityById(articleId)
        val sellerEntity = userService.getUserEntityById(sellerId)
        val buyerEntity = userService.getUserEntityById(buyerId)
        val chatRoomEntity =
            ChatRoomEntity(
                article = articleEntity,
                seller = sellerEntity,
                buyer = buyerEntity,
                updatedAt = Instant.now(),
            )
        chatRoomRepository.save(chatRoomEntity)
        return ChatRoom.fromEntity(chatRoomEntity, "")
    }

    @Transactional
    fun findAllByArticleId(articleId: Long): List<ChatRoomEntity> {
        return chatRoomRepository.findAllByArticleId(articleId)
    }
}
