package com.toyProject7.karrot.chatRoom.controller

import com.toyProject7.karrot.chatRoom.service.ChatRoomService
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller

@Controller
class ChatController(
    private val chatRoomService: ChatRoomService,
    private val messagingTemplate: SimpMessagingTemplate,
) {
    @MessageMapping("/chat/sendMessage") // /app/chat/sendMessage로 받은 메시지를
    fun sendMessage(chatMessage: ChatMessage) {
        val savedMessage = chatRoomService.saveMessage(chatMessage)
        // /topic/chat/${chatMessage.chatRoomId} 여기로 전달
        messagingTemplate.convertAndSend("/topic/chat/${chatMessage.chatRoomId}", savedMessage)
    }
}
