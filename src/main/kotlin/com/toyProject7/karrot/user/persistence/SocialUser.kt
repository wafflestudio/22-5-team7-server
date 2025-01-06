package com.toyProject7.karrot.user.persistence

import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("SOCIAL") // Value for the user_type column for social users
class SocialUser(
    nickname: String,
    location: String,
    temperature: Double,
    email: String,

    @Column(name = "provider")
    var provider: String,
    @Column(name = "providerId")
    var providerId: String,
) : UserEntity(nickname = nickname, location = location, temperature = temperature, email = email) {
    // You can add additional methods specific to social users here
}