package com.toyProject7.karrot.manner.service

import com.toyProject7.karrot.manner.UserNotFoundException
import com.toyProject7.karrot.manner.controller.MannerType
import com.toyProject7.karrot.manner.persistence.MannerEntity
import com.toyProject7.karrot.manner.persistence.MannerRepository
import com.toyProject7.karrot.user.controller.User
import com.toyProject7.karrot.user.persistence.UserRepository
import org.springframework.stereotype.Service

@Service
class MannerService(
    private val mannerRepository: MannerRepository,
    private val userRepository: UserRepository,
) {
    fun increaseMannerCount(
        nickname: String,
        mannerType: MannerType,
    ) {
        val userEntity = userRepository.findByNickname(nickname) ?: throw UserNotFoundException()
        val user = User.fromEntity(userEntity)

        val mannerEntity = mannerRepository.findByUserIdAndMannerType(user.id, mannerType)
        if (mannerEntity != null) {
            mannerEntity.count++
            mannerRepository.save(mannerEntity)
        } else {
            val newMannerEntity =
                MannerEntity(
                    user = userEntity,
                    mannerType = mannerType,
                    count = 1,
                )
            mannerRepository.save(newMannerEntity)
        }
    }
}
