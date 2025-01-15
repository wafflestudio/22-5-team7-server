package com.toyProject7.karrot.profile.persistence

import com.toyProject7.karrot.manner.persistence.MannerEntity
import com.toyProject7.karrot.review.persistence.ReviewEntity
import com.toyProject7.karrot.user.persistence.UserEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne

@Entity(name = "profile")
class ProfileEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    var user: UserEntity,
    @OneToMany(mappedBy = "profile", cascade = [CascadeType.ALL])
    var manners: MutableList<MannerEntity> = mutableListOf(),
    @ManyToMany
    @JoinTable(
        name = "profile_review",
        joinColumns = [JoinColumn(name = "profile_id")],
        inverseJoinColumns = [JoinColumn(name = "review_id")]
    )
    var reviews: MutableList<ReviewEntity> = mutableListOf(),
)
