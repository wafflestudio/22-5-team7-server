package com.toyProject7.karrot.auction.persistence

import com.toyProject7.karrot.image.persistence.ImageUrlEntity
import com.toyProject7.karrot.user.persistence.UserEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import java.time.Instant

@Entity(name = "auctions")
class AuctionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    var seller: UserEntity,
    @ManyToOne
    @JoinColumn(name = "bidder_id", nullable = true)
    var bidder: UserEntity?,
    @Column(name = "title", nullable = false)
    var title: String,
    @Column(name = "content", nullable = false)
    var content: String,
    @Column(name = "tag", nullable = false)
    var tag: String,
    @Column(name = "starting_price", nullable = false)
    var startingPrice: Int,
    @Column(name = "current_price", nullable = false)
    var currentPrice: Int,
    @Column(name = "status", nullable = false)
    var status: Int,
    @Column(name = "location", nullable = false)
    var location: String,
    @OneToMany(mappedBy = "auction")
    var imageUrls: MutableList<ImageUrlEntity> = mutableListOf(),
    @OneToMany(mappedBy = "auction")
    var auctionLikes: MutableList<AuctionLikesEntity> = mutableListOf(),
    @Column(name = "starting_time", nullable = false)
    var startingTime: Instant,
    @Column(name = "end_time", nullable = false)
    var endTime: Instant,
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant,
    @Column(name = "view_count")
    var viewCount: Int,
    @OneToMany(mappedBy = "auction", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var participations: MutableList<AuctionParticipantEntity> = mutableListOf(),
)
