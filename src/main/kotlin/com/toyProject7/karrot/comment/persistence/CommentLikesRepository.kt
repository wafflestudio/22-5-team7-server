package com.toyProject7.karrot.comment.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface CommentLikesRepository : JpaRepository<CommentLikesEntity, String>
