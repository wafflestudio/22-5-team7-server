package com.toyProject7.karrot.review.persistence

import com.toyProject7.karrot.user.persistence.UserEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.Instant

@Entity(name = "review")
class ReviewEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(columnDefinition = "TEXT")
    var content: String,
    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    var seller: UserEntity,
    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    var buyer: UserEntity,
    @Column(name = "location", nullable = false)
    var location: String,
    @Column(name = "created_at", nullable = false)
    var createdAt: Instant,
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant,
)
