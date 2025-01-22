package com.toyProject7.karrot.profile.service

import com.toyProject7.karrot.article.controller.Article
import com.toyProject7.karrot.article.controller.Item
import com.toyProject7.karrot.article.service.ArticleService
import com.toyProject7.karrot.image.persistence.ImageUrlEntity
import com.toyProject7.karrot.image.service.ImageService
import com.toyProject7.karrot.manner.controller.Manner
import com.toyProject7.karrot.profile.ProfileNotFoundException
import com.toyProject7.karrot.profile.controller.EditProfileRequest
import com.toyProject7.karrot.profile.controller.Profile
import com.toyProject7.karrot.profile.persistence.ProfileEntity
import com.toyProject7.karrot.profile.persistence.ProfileRepository
import com.toyProject7.karrot.review.controller.Review
import com.toyProject7.karrot.review.service.ReviewService
import com.toyProject7.karrot.user.controller.User
import com.toyProject7.karrot.user.persistence.UserEntity
import com.toyProject7.karrot.user.service.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class ProfileService(
    private val profileRepository: ProfileRepository,
    private val userService: UserService,
    private val reviewService: ReviewService,
    private val articleService: ArticleService,
    private val imageService: ImageService,
) {
    @Transactional
    fun getMyProfile(user: User): Profile {
        val profileEntity = profileRepository.findByUserId(user.id) ?: throw ProfileNotFoundException()
        val itemCount = getItemCount(user.id)

        val userEntity = userService.getUserEntityById(user.id)
        refreshPresignedUrlIfExpired(userEntity)
        profileRepository.save(profileEntity)

        return Profile.fromEntity(profileEntity, itemCount)
    }

    @Transactional
    fun getProfile(nickname: String): Profile {
        val userEntity = userService.getUserEntityByNickname(nickname)
        refreshPresignedUrlIfExpired(userEntity)
        val user = User.fromEntity(userEntity)
        val profileEntity = profileRepository.findByUserId(user.id) ?: throw ProfileNotFoundException()
        val itemCount = getItemCount(user.id)
        return Profile.fromEntity(profileEntity, itemCount)
    }

    @Transactional
    fun getProfileSells(
        nickname: String,
        articleId: Long,
    ): List<Item> {
        val user = userService.getUserEntityByNickname(nickname)
        val articles = articleService.getArticlesBySeller(user.id!!, articleId)
        val itemList =
            articles.map { article ->
                Item.fromArticle(Article.fromEntity(article))
            }
        return itemList
    }

    @Transactional
    fun editProfile(
        user: User,
        request: EditProfileRequest,
    ): Profile {
        val userEntity = userService.getUserEntityById(user.id)
        val profileEntity = profileRepository.findByUserId(user.id) ?: throw ProfileNotFoundException()
        val itemCount = getItemCount(user.id)

        userEntity.nickname = request.nickname
        userEntity.location = request.location
        if (userEntity.imageUrl != null) {
            val imageUrlListForDel: MutableList<ImageUrlEntity> = mutableListOf()
            imageUrlListForDel += userEntity.imageUrl!!
            imageService.deleteImageUrl(imageUrlListForDel)
            userEntity.imageUrl = null
        }

        if (request.imageCount > 0) {
            val imageUrlEntity: ImageUrlEntity = imageService.postImageUrl("user", profileEntity.id!!, 1)
            val imagePutPresignedUrl: String = imageService.generatePutPresignedUrl(imageUrlEntity.s3)
            imageService.generateGetPresignedUrl(imageUrlEntity)
            userEntity.imageUrl = imageUrlEntity
            profileRepository.save(profileEntity)

            val profile = Profile.fromEntity(profileEntity, itemCount)
            profile.user.imagePresignedUrl = imagePutPresignedUrl

            return profile
        } else {
            profileRepository.save(profileEntity)
            return Profile.fromEntity(profileEntity, itemCount)
        }
    }

    @Transactional
    fun refreshPresignedUrlIfExpired(userEntity: UserEntity) {
        if (userEntity.imageUrl != null && ChronoUnit.MINUTES.between(userEntity.updatedAt, Instant.now()) >= 10) {
            imageService.generateGetPresignedUrl(userEntity.imageUrl!!)
            userEntity.updatedAt = Instant.now()
        }
    }

    @Transactional
    fun getManner(nickname: String): List<Manner> {
        val userEntity = userService.getUserEntityByNickname(nickname)
        val user = User.fromEntity(userEntity)
        val profileEntity = profileRepository.findByUserId(user.id) ?: throw ProfileNotFoundException()
        return Profile.fromEntity(profileEntity, 0).manners
    }

    @Transactional
    fun getPreviousReviews(
        nickname: String,
        reviewId: Long,
    ): List<Review> {
        return reviewService.getPreviousReviews(nickname, reviewId)
    }

    fun getItemCount(id: String): Int {
        return articleService.getItemCount(id)
    }

    @Transactional
    fun getProfileEntityByUserId(userId: String): ProfileEntity {
        return profileRepository.findByUserId(userId) ?: throw ProfileNotFoundException()
    }
}
