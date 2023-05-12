package org.fundatec.vinilemess.tcc.fintrackapp.repository

import android.content.Context
import org.fundatec.vinilemess.tcc.fintrackapp.data.User
import org.fundatec.vinilemess.tcc.fintrackapp.data.local.datasource.UserDataSource

class UserRepository(private val context: Context) {
    private val userDataSource by lazy {
        UserDataSource(context)
    }

    suspend fun upsertUser(user: User) {
        userDataSource.upsertUser(user)
    }

    suspend fun findUser(): User? = userDataSource.findUser()
}