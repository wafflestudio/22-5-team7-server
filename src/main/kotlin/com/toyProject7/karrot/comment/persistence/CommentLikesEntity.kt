package com.toyProject7.karrot.comment.persistence

import com.toyProject7.karrot.user.persistence.UserEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.Instant

@Entity(name = "comment_likes")
class CommentLikesEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: UserEntity,
    @ManyToOne
    @JoinColumn(name = "comment_id")
    var comment: CommentEntity,
    @Column(name = "created_at", nullable = false)
    var createdAt: Instant,
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant,
)
