package com.toyProject7.karrot.feed.service

import com.toyProject7.karrot.feed.persistence.FeedLikesRepository
import com.toyProject7.karrot.feed.persistence.FeedRepository
import org.springframework.stereotype.Service

@Service
class FeedService(
    private val feedRepository: FeedRepository,
    private val feedLikesRepository: FeedLikesRepository,
) {

}
