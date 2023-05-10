package org.fundatec.vinilemess.tcc.fintrackapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.fundatec.vinilemess.tcc.fintrackapp.usecase.LoginUsecase
import kotlinx.coroutines.launch
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.request.UserRequest

class ProfileViewModel: ViewModel() {
    private val usecase: LoginUsecase by lazy {
        LoginUsecase()
    }

    fun saveUser(name:String, email:String, password:String) {
        viewModelScope.launch {
            usecase.registerUser(UserRequest(name, email, password))
        }
    }
}

data class User(val name:String, val email:String, val password:String)