package com.toyProject7.karrot.user.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UserRepository : JpaRepository<UserEntity, String> {
    @Query("SELECT n FROM NormalUser n WHERE n.userId = :userId")
    fun findNormalUserByUserId(
        @Param("userId") userId: String,
    ): NormalUser?

    fun findByNickname(nickname: String): UserEntity?

    @Query("SELECT s FROM SocialUser s WHERE s.email = :email")
    fun findSocialUserByEmail(
        @Param("email") email: String,
    ): SocialUser?

    fun existsByNickname(nickname: String): Boolean

    @Query("SELECT COUNT (n) > 0 FROM NormalUser n WHERE n.userId = :userId")
    fun existsByUserId(
        @Param("userId") userId: String,
    ): Boolean
}
