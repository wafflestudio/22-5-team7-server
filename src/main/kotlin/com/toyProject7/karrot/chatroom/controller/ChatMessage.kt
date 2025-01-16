package com.toyProject7.karrot.chatroom.controller

import com.toyProject7.karrot.user.controller.User

data class ChatMessage(
    val type: MessageType,
    val content: String?,
    val sender: User,
) {
    enum class MessageType {
        CHAT,
        JOIN,
        LEAVE,
    }
}
