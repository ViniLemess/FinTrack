package org.fundatec.vinilemess.tcc.personalbank.user.service

import org.fundatec.vinilemess.tcc.personalbank.user.domain.User
import org.fundatec.vinilemess.tcc.personalbank.user.domain.request.UserRequest
import org.fundatec.vinilemess.tcc.personalbank.user.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(private val userRepository: UserRepository) {

    fun registerUser(newUser: UserRequest) = userRepository.save(newUser.buildUser())

    private fun UserRequest.buildUser() = User(
        id = null,
        name = this.name,
        email = this.email,
        userIdentifier = UUID.randomUUID().toString()
    )
}