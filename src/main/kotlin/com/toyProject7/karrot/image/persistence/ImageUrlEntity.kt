package com.toyProject7.karrot.image.persistence

import com.toyProject7.karrot.article.persistence.ArticleEntity
import com.toyProject7.karrot.feed.persistence.FeedEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity(name = "image_urls")
class ImageUrlEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne
    @JoinColumn(name = "article_id")
    var article: ArticleEntity? = null,
    @ManyToOne
    @JoinColumn(name = "feed_id")
    var feed: FeedEntity? = null,
//    @ManyToOne
//    @JoinColumn(name = "auction_id")
//    var auction: AuctionEntity? = null,
    @Column(name = "s3", nullable = false, length = 512)
    var s3: String,
    @Column(name = "presigned", nullable = false, length = 512)
    var presigned: String = "",
)
