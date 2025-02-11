package com.toyProject7.karrot.article.persistence

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

@Entity(name = "articles")
class ArticleEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne
    @JoinColumn(name = "seller_id")
    var seller: UserEntity,
    @ManyToOne
    @JoinColumn(name = "buyer_id")
    var buyer: UserEntity?,
    @Column(name = "title", nullable = false)
    var title: String,
    @Column(name = "content", nullable = false)
    var content: String,
    @Column(name = "tag", nullable = false)
    var tag: String,
    @Column(name = "price", nullable = false)
    var price: Int,
    @Column(name = "status", nullable = false)
    var status: Int,
    @Column(name = "location", nullable = false)
    var location: String,
    @OneToMany(mappedBy = "article")
    var imageUrls: MutableList<ImageUrlEntity> = mutableListOf(),
    @OneToMany(mappedBy = "article")
    var articleLikes: MutableList<ArticleLikesEntity> = mutableListOf(),
    @Column(name = "created_at", nullable = false)
    var createdAt: Instant,
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant,
    @Column(name = "view_count")
    var viewCount: Int,
    @Column(name = "is_dummy")
    var isDummy: Int,
)
