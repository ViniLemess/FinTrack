package org.fundatec.vinilemess.tcc.fintrackapp.repository

import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.datasource.ErrorModel
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.datasource.LoginDataSource
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.datasource.Result
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.request.UserRequest
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.response.UserResponse

class LoginRepository {

    private val remoteDataSource by lazy {
        LoginDataSource()
    }

    suspend fun login(email:String, password:String): Result<UserResponse, ErrorModel> {
        return remoteDataSource.login(email = email, password = password)
    }

    suspend fun registerUser(user: UserRequest) {
        remoteDataSource.registerUser(user)
    }
}