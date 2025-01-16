package com.toyProject7.karrot.manner.controller

import com.toyProject7.karrot.manner.persistence.MannerEntity

data class Manner(
    val mannerType: MannerType,
    val count: Int,
) {
    companion object {
        fun fromEntity(entity: MannerEntity): Manner {
            return Manner(
                mannerType = entity.mannerType,
                count = entity.count,
            )
        }
    }
}
