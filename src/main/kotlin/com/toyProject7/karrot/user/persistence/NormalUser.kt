package com.toyProject7.karrot.user.persistence

import com.toyProject7.karrot.image.persistence.ImageUrlEntity
import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import java.time.Instant

@Entity
@DiscriminatorValue("NORMAL")
class NormalUser(
    nickname: String,
    location: String,
    temperature: Double,
    email: String,
    imageUrl: ImageUrlEntity,
    updatedAt: Instant,
    @Column(name = "user_id")
    var userId: String,
    @Column(name = "hashed_password")
    var hashedPassword: String = "",
) : UserEntity(
        nickname = nickname,
        location = location,
        temperature = temperature,
        email = email,
        imageUrl = imageUrl,
        updatedAt = updatedAt,
    ) {
    // Additional attributes and methods for normal users can go here
}
