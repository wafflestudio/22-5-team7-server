package com.toyProject7.karrot.chatRoom.controller

import com.toyProject7.karrot.chatRoom.service.ChatRoomService
import com.toyProject7.karrot.user.AuthUser
import com.toyProject7.karrot.user.controller.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/api")
class ChatRoomController(
    private val chatRoomService: ChatRoomService,
) {
    @GetMapping("/chat")
    fun getRooms(
        @AuthUser user: User,
        @RequestParam("updatedAt") updatedAt: Instant,
    ): ResponseEntity<List<ChatRoom>> {
        val chatRooms: List<ChatRoom> = chatRoomService.getChatRooms(user, updatedAt)
        return ResponseEntity.ok(chatRooms)
    }

    @GetMapping("/chat/{chatRoomId}")
    fun getRoom(
        @PathVariable chatRoomId: Long,
        @AuthUser user: User,
        @RequestParam("createdAt") createdAt: Instant,
    ): ResponseEntity<ChatRoomResponse> {
        val chatRoomResponse = chatRoomService.getChatRoom(chatRoomId, user, createdAt)
        return ResponseEntity.ok(chatRoomResponse)
    }

    @PostMapping("/chat/create")
    fun createRoom(
        @RequestBody request: CreateChatRoomRequest,
        @AuthUser user: User,
    ): ResponseEntity<ChatRoom> {
        val chatRoom = chatRoomService.createChatRoom(user, request.articleId, request.sellerId, request.buyerId)
        return ResponseEntity.ok(chatRoom)
    }
}

data class CreateChatRoomRequest(
    val articleId: Long,
    val sellerId: String,
    val buyerId: String,
)

data class ChatRoomResponse(
    val chatRoom: ChatRoom,
    val messages: List<ChatMessage>,
)
