package com.toyProject7.karrot.user.service

import com.toyProject7.karrot.profile.persistence.ProfileEntity
import com.toyProject7.karrot.profile.persistence.ProfileRepository
import com.toyProject7.karrot.user.AuthenticateException
import com.toyProject7.karrot.user.SignInInvalidPasswordException
import com.toyProject7.karrot.user.SignInUserNotFoundException
import com.toyProject7.karrot.user.SignUpBadNicknameException
import com.toyProject7.karrot.user.SignUpBadPasswordException
import com.toyProject7.karrot.user.SignUpBadUserIdException
import com.toyProject7.karrot.user.SignUpInvalidEmailException
import com.toyProject7.karrot.user.SignUpNicknameConflictException
import com.toyProject7.karrot.user.SignUpUserIdConflictException
import com.toyProject7.karrot.user.UserAccessTokenUtil
import com.toyProject7.karrot.user.UserNotFoundException
import com.toyProject7.karrot.user.controller.User
import com.toyProject7.karrot.user.persistence.NormalUser
import com.toyProject7.karrot.user.persistence.NormalUserRepository
import com.toyProject7.karrot.user.persistence.SocialUser
import com.toyProject7.karrot.user.persistence.UserEntity
import com.toyProject7.karrot.user.persistence.UserPrincipal
import com.toyProject7.karrot.user.persistence.UserRepository
import org.mindrot.jbcrypt.BCrypt
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class UserService(
    private val userRepository: UserRepository,
    private val profileRepository: ProfileRepository,
    private val normalUserRepository: NormalUserRepository,
) {
    @Transactional
    fun signUp(
        nickname: String,
        userId: String,
        password: String,
        email: String,
    ): User {
        if (userId.length < 5 || userId.length > 20) {
            throw SignUpBadUserIdException()
        }
        if (password.length < 8 || password.length > 16) {
            throw SignUpBadPasswordException()
        }
        if (nickname.length < 2 || nickname.length > 10) {
            throw SignUpBadNicknameException()
        }
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        if (!emailRegex.matches(email)) {
            throw SignUpInvalidEmailException()
        }

        if (normalUserRepository.existsByUserId(userId)) {
            throw SignUpUserIdConflictException()
        }
        if (userRepository.existsByNickname(nickname)) {
            throw SignUpNicknameConflictException()
        }
        val encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        val user =
            userRepository.save(
                NormalUser(
                    nickname = nickname,
                    userId = userId,
                    hashedPassword = encryptedPassword,
                    location = "void",
                    temperature = 36.5,
                    email = email,
                    imageUrl = null,
                    updatedAt = Instant.now(),
                ),
            )

        val profileEntity =
            ProfileEntity(
                user = user,
            )
        profileRepository.save(profileEntity)

        return User.fromEntity(user)
    }

    @Transactional
    fun signIn(
        userId: String,
        password: String,
    ): Pair<User, String> {
        val targetUser = userRepository.findNormalUserByUserId(userId) ?: throw SignInUserNotFoundException()
        if (!BCrypt.checkpw(password, targetUser.hashedPassword)) {
            throw SignInInvalidPasswordException()
        }
        val accessToken = UserAccessTokenUtil.generateAccessToken(targetUser.id!!)
        return Pair(User.fromEntity(targetUser), accessToken)
    }

    @Transactional
    fun authenticate(accessToken: String): User {
        val id = UserAccessTokenUtil.validateAccessTokenGetUserId(accessToken) ?: throw AuthenticateException()
        val user = userRepository.findNormalUserById(id) ?: throw AuthenticateException()
        return User.fromEntity(user)
    }

    @Transactional
    fun createOrRetrieveSocialUser(
        email: String,
        providerId: String,
        provider: String,
        username: String,
    ): User {
        // Check if the user exists by email
        val existingUser = userRepository.findSocialUserByEmail(email)

        return existingUser?.let {
            // Convert existingUser (of type SocialUser) to User DTO
            User.fromEntity(it)
        } ?: run {
            // If the user doesn't exist, create a new one
            val newUser =
                SocialUser(
                    email = email,
                    nickname = username,
                    provider = provider,
                    providerId = providerId,
                    location = "void",
                    temperature = 36.5,
                    imageUrl = null,
                    updatedAt = Instant.now(),
                )
            val savedUser = userRepository.save(newUser) // This should save as SocialUser

            val profileEntity =
                ProfileEntity(
                    user = newUser,
                )

            profileRepository.save(profileEntity)

            User.fromEntity(savedUser) // Convert and return as User DTO
        }
    }

    @Transactional
    fun getUserEntityById(id: String): UserEntity {
        return userRepository.findByIdOrNull(id) ?: throw UserNotFoundException()
    }

    @Transactional
    fun loadUserPrincipalById(id: String): UserPrincipal {
        val user =
            userRepository.findById(id)
                .orElseThrow { UsernameNotFoundException("User not found with id: $id") }
        return UserPrincipal.create(user)
    }

    @Transactional
    fun getUserEntityByNickname(nickname: String): UserEntity {
        return userRepository.findByNickname(nickname) ?: throw UserNotFoundException()
    }
}
