package com.toyProject7.karrot.user.persistence

import com.toyProject7.karrot.image.persistence.ImageUrlEntity
import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import java.time.Instant

@Entity
@DiscriminatorValue("SOCIAL") // Value for the user_type column for social users
class SocialUser(
    nickname: String,
    location: String,
    temperature: Double,
    email: String,
    imageUrl: ImageUrlEntity?,
    updatedAt: Instant,
    @Column(name = "provider")
    var provider: String,
    @Column(name = "provider_id")
    var providerId: String,
) : UserEntity(
        nickname = nickname,
        location = location,
        temperature = temperature,
        email = email,
        imageUrl = imageUrl,
        updatedAt = updatedAt,
    ) {
    // You can add additional methods specific to social users here
}
