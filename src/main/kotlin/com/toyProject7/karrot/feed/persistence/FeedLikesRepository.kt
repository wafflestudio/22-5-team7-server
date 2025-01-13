package com.toyProject7.karrot.feed.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface FeedLikesRepository : JpaRepository<FeedLikesEntity, String>