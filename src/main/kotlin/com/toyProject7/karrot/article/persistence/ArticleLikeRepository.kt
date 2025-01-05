package com.toyProject7.karrot.article.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface ArticleLikeRepository: JpaRepository<ArticleLikeEntity, String> {
    fun findAllByUserId(userId: String): List<ArticleLikeEntity>
}

