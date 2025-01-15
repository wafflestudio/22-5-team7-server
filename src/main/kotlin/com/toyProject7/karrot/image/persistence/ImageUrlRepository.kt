package com.toyProject7.karrot.image.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface ImageUrlRepository : JpaRepository<ImageUrlEntity, String>
