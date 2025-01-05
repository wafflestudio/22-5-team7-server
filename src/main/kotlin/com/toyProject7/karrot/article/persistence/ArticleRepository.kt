package com.toyProject7.karrot.article.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface ArticleRepository: JpaRepository<ArticleEntity, String> {
    fun findAllByBuyerId(userId: String): List<ArticleEntity>

    fun findAllBySellerId(userId: String): List<ArticleEntity>
}
