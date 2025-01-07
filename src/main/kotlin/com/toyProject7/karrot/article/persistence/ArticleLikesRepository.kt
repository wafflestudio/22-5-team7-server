package com.toyProject7.karrot.article.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface ArticleLikesRepository : JpaRepository<ArticleLikesEntity, String>
