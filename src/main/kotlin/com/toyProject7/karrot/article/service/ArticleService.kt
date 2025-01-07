package com.toyProject7.karrot.article.service

import com.toyProject7.karrot.article.controller.Article
import com.toyProject7.karrot.article.controller.Item
import com.toyProject7.karrot.article.controller.SellingAndSoldItemsResponse
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
    fun listLikedArticles(userId: String): List<Item> {
        val articleLikes = articleLikeRepository.findAllByUserId(userId)
        return articleLikes
            .map { articleLikeEntity -> Article.fromEntity(articleLikeEntity.article) }
            .map { article -> Item.fromArticle(article) }
            .sortedBy { it.createdAt }
    }

    fun listSellingAndSoldArticles(userId: String): SellingAndSoldItemsResponse {
        val articles = articleRepository.findAllBySellerId(userId)
        val sellingArticles =
            articles.filter { !it.isSelled }
                .map { Article.fromEntity(it) }
                .map { Item.fromArticle(it) }

        val soldArticles =
            articles.filter { it.isSelled }
                .map { Article.fromEntity(it) }
                .map { Item.fromArticle(it) }

        return SellingAndSoldItemsResponse(
            sellingArticles,
            soldArticles,
        )
    }

    fun listBoughtArticles(userId: String): List<Item> {
        return articleRepository.findAllByBuyerId(userId)
            .map { articleEntity -> Article.fromEntity(articleEntity) }
            .map { article -> Item.fromArticle(article) }
            .sortedBy { it.createdAt }
    }
}
