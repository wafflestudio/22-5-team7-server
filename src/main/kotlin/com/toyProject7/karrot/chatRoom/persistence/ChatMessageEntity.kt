package com.toyProject7.karrot.chatRoom.persistence

import com.toyProject7.karrot.user.persistence.UserEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.Instant

@Entity(name = "chat_messages")
class ChatMessageEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne
    @JoinColumn(name = "chat_room_id", nullable = false)
    var chatRoom: ChatRoomEntity,
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    var sender: UserEntity,
    @Column(name = "content", nullable = false)
    var content: String,
    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now(),
)
