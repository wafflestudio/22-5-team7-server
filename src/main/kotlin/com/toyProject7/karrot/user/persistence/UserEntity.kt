package com.toyProject7.karrot.user.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name = "users")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    @Column(name = "nickname", nullable = false)
    var nickname: String,
    @Column(name = "user_id", nullable = false)
    var userId: String,
    @Column(name = "hashed_password", nullable = false)
    var hashedPassword: String,
    @Column(name = "location")
    var location: String,
    @Column(name = "temperature")
    var temperature: Double,
    @Column(name = "email")
    var email: String,
)
