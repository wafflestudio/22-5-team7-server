package com.toyProject7.karrot.image.persistence

import com.toyProject7.karrot.article.persistence.ArticleEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity(name = "image_url")
class ImageUrlEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne
    @JoinColumn(name = "article_id")
    var article: ArticleEntity,
    @Column(name = "url", nullable = false, length = 512)
    var url: String,
)
