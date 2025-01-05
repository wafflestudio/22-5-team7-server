package com.toyProject7.karrot.article.service

import com.toyProject7.karrot.article.controller.Article
import com.toyProject7.karrot.article.controller.SellingAndSoldArticlesResponse
import com.toyProject7.karrot.article.persistence.ArticleLikeRepository
import com.toyProject7.karrot.article.persistence.ArticleRepository
import com.toyProject7.karrot.user.persistence.UserRepository
import org.springframework.stereotype.Service

@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val articleLikeRepository: ArticleLikeRepository,
    private val userRepository: UserRepository,
) {
    fun listLikedArticles(userId: String): List<Article> {
        val articleLikes = articleLikeRepository.findAllByUserId(userId)
        return articleLikes
            .map { articleLikeEntity -> Article.fromEntity(articleLikeEntity.article) }
            .sortedBy { it.createdAt }
    }

    fun listSellingAndSoldArticles(userId: String): SellingAndSoldArticlesResponse {
        val articles = articleRepository.findAllBySellerId(userId)
        val sellingArticles = articles.filter { !it.isSelled }
            .map { Article.fromEntity(it) }

        val soldArticles = articles.filter { it.isSelled }
            .map { Article.fromEntity(it) }

        return SellingAndSoldArticlesResponse(
            sellingArticles,
            soldArticles
        )
    }

    fun listBoughtArticles(userId: String): List<Article> {
        return articleRepository.findAllByBuyerId(userId)
            .map { articleEntity -> Article.fromEntity(articleEntity) }
            .sortedBy { it.createdAt }
    }
}
