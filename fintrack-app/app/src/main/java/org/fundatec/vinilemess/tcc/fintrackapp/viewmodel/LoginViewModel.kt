package org.fundatec.vinilemess.tcc.fintrackapp.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.fundatec.vinilemess.tcc.fintrackapp.usecase.LoginUsecase
import kotlinx.coroutines.launch
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.datasource.ErrorModel
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.datasource.Result
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.response.UserResponse
import org.fundatec.vinilemess.tcc.fintrackapp.toModel
import org.fundatec.vinilemess.tcc.fintrackapp.usecase.UserUseCase

class LoginViewModel(private val context: Context) : ViewModel() {

    private val loginUsecase by lazy {
        LoginUsecase()
    }

    private val userUseCase by lazy {
        UserUseCase(context)
    }

    private val error: MutableLiveData<Boolean> = MutableLiveData(false)
    private val home: MutableLiveData<Boolean> = MutableLiveData(false)
    val shouldShowError: LiveData<Boolean> = error
    val shouldShowHome: LiveData<Boolean> = home

    fun login(email: String?, password: String?) {
        viewModelScope.launch {
            if (email != null && password != null) {
                val loginResult = executeLogin(email, password)
                val userResponse: UserResponse? = loginResult.get()
                if (userResponse != null) {
                    userUseCase.upsertUser(userResponse.toModel())
                    home.value = true
                    return@launch
                }
                error.value = true
                return@launch
            }
            error.value = true
        }
    }

    private suspend fun executeLogin(email: String, password: String): Result<UserResponse, ErrorModel> {
        val user = loginUsecase.login(email = email, password = password)
        Log.d("Login: ", user.get().toString())
        return user
    }
}