package com.toyProject7.karrot.manner.persistence

import com.toyProject7.karrot.manner.controller.MannerType
import com.toyProject7.karrot.profile.persistence.ProfileEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity(name = "manner")
class MannerEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    var profile: ProfileEntity,
    @Enumerated(EnumType.STRING)
    @Column(name = "manner_type", nullable = false)
    var mannerType: MannerType,
    @Column(name = "count", nullable = false)
    var count: Int = 0,
)
