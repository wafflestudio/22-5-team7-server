package com.toyProject7.karrot.chatroom.controller

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatroomController {
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    fun sendMessage(chatMessage: ChatMessage): ChatMessage {
        return chatMessage
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    fun addUser(chatMessage: ChatMessage): ChatMessage {
        return chatMessage.copy(content = "${chatMessage.sender.nickname}님이 입장했습니다.")
    }
}
