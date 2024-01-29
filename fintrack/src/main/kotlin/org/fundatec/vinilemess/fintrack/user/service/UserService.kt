package org.fundatec.vinilemess.fintrack.user.service

import org.fundatec.vinilemess.fintrack.exception.EmailTakenException
import org.fundatec.vinilemess.fintrack.exception.UnauthorizedException
import org.fundatec.vinilemess.fintrack.user.domain.Role
import org.fundatec.vinilemess.fintrack.user.domain.User
import org.fundatec.vinilemess.fintrack.user.domain.request.LoginRequest
import org.fundatec.vinilemess.fintrack.user.domain.request.UserRequest
import org.fundatec.vinilemess.fintrack.user.domain.response.UserResponse
import org.fundatec.vinilemess.fintrack.user.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(private val userRepository: UserRepository) {

    fun registerUser(newUser: UserRequest): UserResponse {
        isEmailTaken(newUser.email)
        val user = newUser.buildUser()
        return userRepository.save(user).buildResponse()
    }

    fun login(loginRequest: LoginRequest): UserResponse {
        return userRepository.findByCredentials(loginRequest.email, loginRequest.password)?.buildResponse()
                ?: throw UnauthorizedException("Password or email incorrects")
    }

    fun existsSignature(userSignature: String): Boolean =
            userRepository.existsSignature(userSignature)

    private fun isEmailTaken(email: String) {
        val user: User? = userRepository.findByEmail(email)
        if (user != null) {
            throw EmailTakenException("$email has already been taken")
        }
    }

    private fun UserRequest.buildUser() = User(
            id = null,
            name = this.name,
            email = this.email,
            password = this.password,
            transactionSignature = UUID.randomUUID().toString(),
            role = Role.USER,
            listOf()
    )

    private fun User.buildResponse() = UserResponse(this.name, this.email, this.transactionSignature)
}