package com.toyProject7.karrot.article.persistence

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

@Entity(name = "article")
class ArticleEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne
    @JoinColumn(name = "seller")
    var seller: UserEntity,
    @ManyToOne
    @JoinColumn(name = "buyer")
    var buyer: UserEntity?,
    @Column(name = "title", nullable = false)
    var title: String,
    @Column(name = "content", nullable = false)
    var content: String,
    @Column(name = "price", nullable = false)
    var price: Int,
    @Column(name = "status", nullable = false)
    var status: String,
    @Column(name = "location", nullable = false)
    var location: String,
    @OneToMany(mappedBy = "article")
    var articleLikes: MutableList<ArticleLikesEntity> = mutableListOf(),
    @Column(name = "created_at", nullable = false)
    var createdAt: Instant,
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant,
)
