package org.fundatec.vinilemess.tcc.fintrackapp.data.remote.datasource

import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.response.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.api.LoginApi
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.RetrofitNetworkClient
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.request.UserRequest
import java.lang.Exception

class LoginDataSource {
    private val client = RetrofitNetworkClient.createNetworkClient()
        .create(LoginApi::class.java)

    suspend fun login(email:String, password:String): Result<UserResponse, ErrorModel> {
        return withContext(Dispatchers.IO) {
            try {
                val user = client.login(email = email,password = password)
                Result.Success(user)
            } catch (exception:Exception) {
                Result.Error(ErrorModel)
            }
        }
    }

    suspend fun registerUser(userRequest: UserRequest) {
        withContext(Dispatchers.IO) {
            try {
                client.registerUser(userRequest)
                Result.Success(userRequest)
            } catch (exception:Exception) {
                Result.Error(ErrorModel)
            }
        }
    }
}

object ErrorModel
sealed class Result<out S, out E> {
    data class Success<S>(val value:S): Result<S, Nothing>()
    data class Error<E>(val value:E): Result<Nothing, E>()

    fun get():S? {
        return when(this) {
            is Success -> value
            else -> null
        }
    }
}