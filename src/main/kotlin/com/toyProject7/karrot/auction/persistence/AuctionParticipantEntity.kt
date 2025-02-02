package com.toyProject7.karrot.auction.persistence

import com.toyProject7.karrot.user.persistence.UserEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity(name = "auction_participant")
data class AuctionParticipantEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity,
    @ManyToOne
    @JoinColumn(name = "auction_id", nullable = false)
    val auction: AuctionEntity,
)
