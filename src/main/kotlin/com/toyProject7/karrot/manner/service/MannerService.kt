package com.toyProject7.karrot.manner.service

import com.toyProject7.karrot.article.ArticleIsNotEndException
import com.toyProject7.karrot.article.YouAreNotBuyerOrSellerException
import com.toyProject7.karrot.article.service.ArticleService
import com.toyProject7.karrot.manner.controller.MannerType
import com.toyProject7.karrot.manner.persistence.MannerEntity
import com.toyProject7.karrot.manner.persistence.MannerRepository
import com.toyProject7.karrot.profile.controller.Profile
import com.toyProject7.karrot.profile.service.ProfileService
import com.toyProject7.karrot.user.controller.User
import com.toyProject7.karrot.user.service.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MannerService(
    private val mannerRepository: MannerRepository,
    private val userService: UserService,
    private val profileService: ProfileService,
    private val articleService: ArticleService,
) {
    @Transactional
    fun increaseMannerCount(
        user: User,
        nickname: String,
        mannerType: MannerType,
        articleId: Long,
    ) {
        val userEntity = userService.getUserEntityByNickname(nickname)
        val articleEntity = articleService.getArticleEntityById(articleId)
        if (articleEntity.status != 2) {
            throw ArticleIsNotEndException()
        }
        if (articleEntity.buyer != userService.getUserEntityById(user.id) && articleEntity.buyer != userEntity) {
            throw YouAreNotBuyerOrSellerException()
        }
        val profileEntity = profileService.getProfileEntityByUserId(userEntity.id!!)
        val profile = Profile.fromEntity(profileEntity, 0)

        val mannerEntity = mannerRepository.findByProfileIdAndMannerType(profile.id, mannerType)
        if (mannerEntity != null) {
            mannerEntity.count++
            mannerRepository.save(mannerEntity)
        } else {
            val newMannerEntity =
                MannerEntity(
                    profile = profileEntity,
                    mannerType = mannerType,
                    count = 1,
                )
            mannerRepository.save(newMannerEntity)
            profileEntity.manners += newMannerEntity
        }

        if (mannerType.name.startsWith("NEG_")) {
            userEntity.temperature -= 0.1
        } else {
            userEntity.temperature += 0.1
        }

        if (userEntity.temperature > 100) {
            userEntity.temperature = 100.0
        } else if (userEntity.temperature < 0) {
            userEntity.temperature = 0.0
        }
    }
}
