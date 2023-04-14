package org.fundatec.vinilemess.tcc.fintrack.user.service

import org.fundatec.vinilemess.tcc.fintrack.user.domain.User
import org.fundatec.vinilemess.tcc.fintrack.user.domain.request.UserRequest
import org.fundatec.vinilemess.tcc.fintrack.user.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(private val userRepository: UserRepository) {

    fun registerUser(newUser: UserRequest) = userRepository.save(newUser.buildUser())

    private fun UserRequest.buildUser() = User(
        id = null,
        name = this.name,
        email = this.email,
        userSignature = UUID.randomUUID().toString()
    )
}