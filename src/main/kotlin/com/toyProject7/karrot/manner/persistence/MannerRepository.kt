package com.toyProject7.karrot.manner.persistence

import com.toyProject7.karrot.manner.controller.MannerType
import org.springframework.data.jpa.repository.JpaRepository

interface MannerRepository : JpaRepository<MannerEntity, String> {
    fun findByUserIdAndMannerType(
        id: String,
        mannerType: MannerType,
    ): MannerEntity?
}
