package com.toyProject7.karrot.user.service

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
import com.toyProject7.karrot.user.controller.User
import com.toyProject7.karrot.user.persistence.UserEntity
import com.toyProject7.karrot.user.persistence.UserRepository
import org.mindrot.jbcrypt.BCrypt
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
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

        if (userRepository.existsByUserId(userId)) {
            throw SignUpUserIdConflictException()
        }
        if (userRepository.existsByNickname(nickname)) {
            throw SignUpNicknameConflictException()
        }
        val encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        val user =
            userRepository.save(
                UserEntity(
                    nickname = nickname,
                    userId = userId,
                    hashedPassword = encryptedPassword,
                    location = "void",
                    temperature = 36.5,
                    email = email,
                ),
            )
        return User.fromEntity(user)
    }

    @Transactional
    fun signIn(
        userId: String,
        password: String,
    ): Pair<User, String> {
        val targetUser = userRepository.findByUserId(userId) ?: throw SignInUserNotFoundException()
        if (!BCrypt.checkpw(password, targetUser.hashedPassword)) {
            throw SignInInvalidPasswordException()
        }
        val accessToken = UserAccessTokenUtil.generateAccessToken(targetUser.id!!)
        return Pair(User.fromEntity(targetUser), accessToken)
    }

    @Transactional
    fun authenticate(accessToken: String): User {
        val id = UserAccessTokenUtil.validateAccessTokenGetUserId(accessToken) ?: throw AuthenticateException()
        val user = userRepository.findByIdOrNull(id) ?: throw AuthenticateException()
        return User.fromEntity(user)
    }
}
