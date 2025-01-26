package com.toyProject7.karrot.profile.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface ProfileRepository : JpaRepository<ProfileEntity, String> {
    fun findByUserId(id: String): ProfileEntity?
}
