package com.toyProject7.karrot.user.persistence

import jakarta.persistence.*

@Entity(name = "users")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    @Column(name = "nickname", nullable = false)
    var nickname: String,
    @Column(name = "userId", nullable = false)
    var userId: String,
    @Column(name = "hashed_password", nullable = false)
    var hashedPassword: String,
    @Column(name = "location")
    var location: String,
    @Column(name = "temperature")
    var temperature: Double,
)
