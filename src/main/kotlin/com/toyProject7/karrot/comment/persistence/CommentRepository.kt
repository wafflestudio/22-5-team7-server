package com.toyProject7.karrot.comment.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<CommentEntity, Long>
