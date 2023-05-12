package org.fundatec.vinilemess.tcc.fintrackapp.data.local.datasource

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.fundatec.vinilemess.tcc.fintrackapp.data.User
import org.fundatec.vinilemess.tcc.fintrackapp.data.local.UserDatabase
import org.fundatec.vinilemess.tcc.fintrackapp.toEntity
import org.fundatec.vinilemess.tcc.fintrackapp.toModel

class UserDataSource(private val context: Context) {
    private val database: UserDatabase by lazy {
        UserDatabase.getInstance(context)
    }

    suspend fun upsertUser(user: User) {
        withContext(Dispatchers.IO) {
            database.userDao().insertUser(user.toEntity())
        }
    }

    suspend fun findUser(): User? {
        return withContext(Dispatchers.IO) {
            database.userDao().findUserByUserSignature()
        }?.toModel()
    }
}