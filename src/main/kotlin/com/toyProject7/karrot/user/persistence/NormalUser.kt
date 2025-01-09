package com.toyProject7.karrot.user.persistence

import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("NORMAL")
class NormalUser(
    nickname: String,
    location: String,
    temperature: Double,
    email: String,
    @Column(name = "user_id")
    var userId: String,
    @Column(name = "hashed_password")
    var hashedPassword: String = "",
) : UserEntity(nickname = nickname, location = location, temperature = temperature, email = email) {
    // Additional attributes and methods for normal users can go here
}
