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
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class ChatRoomController(
    private val chatRoomService: ChatRoomService,
) {
    @GetMapping("/chat")
    fun getRooms(
        @AuthUser user: User,
    ): ResponseEntity<List<ChatRoom>> {
        val chatRooms = chatRoomService.getChatRooms(user)
        return ResponseEntity.ok(chatRooms)
    }

    @GetMapping("/chat/{chatRoomId}")
    fun getRoom(
        @PathVariable chatRoomId: Long,
        @AuthUser user: User,
    ): ResponseEntity<List<ChatMessage>> {
        val chatRoom = chatRoomService.getChatRoom(chatRoomId, user)
        return ResponseEntity.ok(chatRoom)
    }

    @PostMapping("/chat/create")
    fun createRoom(
        @AuthUser user: User,
        @RequestBody request: CreateChatRoomRequest,
    ): ResponseEntity<ChatRoom> {
        val chatRoom = chatRoomService.createChatRoom(request.articleId, request.sellerId, request.buyerId)
        return ResponseEntity.ok(chatRoom)
    }
}

data class CreateChatRoomRequest(
    val articleId: Long,
    val sellerId: String,
    val buyerId: String,
)
