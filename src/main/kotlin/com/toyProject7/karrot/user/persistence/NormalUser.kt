package com.toyProject7.karrot.user.persistence

import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
@DiscriminatorValue("NORMAL")
class NormalUser(
    nickname: String,
    location: String,
    temperature: Double,
    email: String,

    @Column(name = "user_id", nullable = false)
    var userId: String,
    @Column(name = "hashed_password", nullable = false)
    var hashedPassword: String,
) : UserEntity(nickname = nickname, location = location, temperature = temperature, email = email) {
    // Additional attributes and methods for normal users can go here
}
