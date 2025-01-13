package com.toyProject7.karrot.feed.persistence

import com.toyProject7.karrot.comment.persistence.CommentEntity
import com.toyProject7.karrot.image.persistence.ImageUrlEntity
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

@Entity(name = "feeds")
class FeedEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne
    @JoinColumn(name = "user_id")
    var author: UserEntity,
    @Column(name = "title", nullable = false)
    var title: String,
    @Column(name = "content", nullable = false)
    var content: String,
    @OneToMany(mappedBy = "feed")
    var imageS3Urls: MutableList<ImageUrlEntity> = mutableListOf(),
    @OneToMany(mappedBy = "feed")
    var imagePresignedUrls: MutableList<ImageUrlEntity> = mutableListOf(),
    @OneToMany(mappedBy = "feed")
    var feedLikes: MutableList<FeedLikesEntity> = mutableListOf(),
    @OneToMany(mappedBy = "feed")
    var feedComments: MutableList<CommentEntity> = mutableListOf(),
    @Column(name = "crated_at", nullable = false)
    var createdAt: Instant,
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant,
    @Column(name = "view_count")
    var viewCount: Int,
    )
