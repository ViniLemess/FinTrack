package org.fundatec.vinilemess.tcc.fintrackapp.usecase

import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.datasource.ErrorModel
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.datasource.Result
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.request.UserRequest
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.response.UserResponse
import org.fundatec.vinilemess.tcc.fintrackapp.repository.LoginRepository

class LoginUsecase {
    private val repository by lazy {
        LoginRepository()
    }

    suspend fun login(email:String, password:String): Result<UserResponse, ErrorModel> {
        return repository.login(email = email, password = password)
    }

    suspend fun registerUser(user: UserRequest) {
        repository.registerUser(user)
    }

}