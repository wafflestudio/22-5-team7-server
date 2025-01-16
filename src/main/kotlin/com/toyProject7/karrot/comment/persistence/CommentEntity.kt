package com.toyProject7.karrot.comment.persistence

import com.toyProject7.karrot.feed.persistence.FeedEntity
import com.toyProject7.karrot.user.persistence.UserEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import java.time.Instant

@Entity(name = "comments")
class CommentEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: UserEntity,
    @ManyToOne
    @JoinColumn(name = "feed_id")
    var feed: FeedEntity,
    @Column(name = "content", nullable = false)
    var content: String,
    @OneToMany(mappedBy = "comment")
    var commentLikes: MutableList<CommentLikesEntity> = mutableListOf(),
    @Column(name = "created_at", nullable = false)
    var createdAt: Instant,
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant,
)
