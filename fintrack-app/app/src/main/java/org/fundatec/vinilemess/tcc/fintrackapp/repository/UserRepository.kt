package org.fundatec.vinilemess.tcc.fintrackapp.repository

import org.fundatec.vinilemess.tcc.fintrackapp.data.User
import org.fundatec.vinilemess.tcc.fintrackapp.data.local.datasource.UserDataSource

class UserRepository {
    private val userDataSource by lazy {
        UserDataSource()
    }

    suspend fun upsertUser(user: User) {
        userDataSource.upsertUser(user)
    }

    suspend fun findUser(): User? = userDataSource.findUser()
}