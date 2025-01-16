package com.toyProject7.karrot.image.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name = "image_urls")
class ImageUrlEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "s3", nullable = false, length = 512)
    var s3: String,
    @Column(name = "presigned", nullable = false, length = 512)
    var presigned: String = "",
)
