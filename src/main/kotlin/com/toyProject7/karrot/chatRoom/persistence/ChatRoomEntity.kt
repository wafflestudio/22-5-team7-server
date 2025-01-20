package com.toyProject7.karrot.chatRoom.persistence

import com.toyProject7.karrot.article.persistence.ArticleEntity
import com.toyProject7.karrot.user.persistence.UserEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.Instant

@Entity(name = "chat_rooms")
class ChatRoomEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    var article: ArticleEntity,
    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    var seller: UserEntity,
    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    var buyer: UserEntity,
    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now(),
)
