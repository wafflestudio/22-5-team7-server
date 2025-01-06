package com.toyProject7.karrot.user.persistence

import com.toyProject7.karrot.user.controller.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UserRepository : JpaRepository<UserEntity, String> {
    @Query("SELECT n FROM NormalUser n WHERE n.userId = :userId")
    fun findNormalUserByUserId(@Param("userId") userId: String): NormalUser?
    @Query("SELECT s FROM NormalUser s WHERE s.id = :id")
    fun findNormalUserById(@Param("id") id: String): NormalUser?
    @Query("SELECT s FROM SocialUser s WHERE s.email = :email")
    fun findSocialUserByEmail(@Param("email") email: String): SocialUser?
}