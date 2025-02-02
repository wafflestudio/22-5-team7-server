package com.toyProject7.karrot.user.persistence

import com.toyProject7.karrot.auction.persistence.AuctionParticipantEntity
import com.toyProject7.karrot.image.persistence.ImageUrlEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorColumn
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "users") // Table name in the database
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Use single table inheritance
@DiscriminatorColumn(name = "user_type") // Column to differentiate user types
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    @Column(name = "nickname", nullable = false)
    var nickname: String,
    @Column(name = "location")
    var location: String,
    @Column(name = "temperature")
    var temperature: Double,
    @Column(name = "email")
    var email: String,
    @OneToOne
    @JoinColumn(name = "image_url_id", nullable = true)
    var imageUrl: ImageUrlEntity? = null,
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant,
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    val participations: MutableList<AuctionParticipantEntity> = mutableListOf(),
)
