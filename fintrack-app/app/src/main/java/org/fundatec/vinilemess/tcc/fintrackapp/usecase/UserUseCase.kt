package org.fundatec.vinilemess.tcc.fintrackapp.usecase

import org.fundatec.vinilemess.tcc.fintrackapp.data.User
import org.fundatec.vinilemess.tcc.fintrackapp.repository.UserRepository

class UserUseCase {
    private val repository by lazy {
        UserRepository()
    }

    suspend fun upsertUser(user: User) {
        repository.upsertUser(user)
    }

    suspend fun findUser(): User? = repository.findUser()
}