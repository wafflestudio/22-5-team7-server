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
import jakarta.persistence.OneToOne
import java.time.Instant

@Entity(name = "articles")
class ArticleEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    @ManyToOne
    @JoinColumn(name = "seller_id")
    var seller: UserEntity,
    @OneToOne
    @JoinColumn(name = "buyer_id")
    var buyer: UserEntity? = null,
    @Column(columnDefinition = "TEXT")
    var title: String,
    @Column(columnDefinition = "TEXT")
    var content: String,
    @Column
    var price: Int,
    @Column(name = "is_selled")
    var isSelled: Boolean,
    @Column
    var location: String,
    @OneToMany(mappedBy = "article")
    var articleLikes: MutableList<ArticleLikeEntity> = mutableListOf(),
    @Column(name = "created_at", nullable = false)
    var createdAt: Instant,
)
