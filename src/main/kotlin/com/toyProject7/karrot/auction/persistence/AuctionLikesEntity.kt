package com.toyProject7.karrot.auction.persistence

import com.toyProject7.karrot.user.persistence.UserEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.Instant

@Entity(name = "auction_likes")
@Table(
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["user_id", "auction_id"]),
    ],
)
class AuctionLikesEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    @ManyToOne
    @JoinColumn(name = "auction_id", nullable = false)
    var auction: AuctionEntity,
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    var user: UserEntity,
    @Column(name = "created_at", nullable = false)
    var createdAt: Instant,
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant,
)
