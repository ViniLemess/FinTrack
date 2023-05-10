package org.fundatec.vinilemess.tcc.fintrackapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.fundatec.vinilemess.tcc.fintrackapp.usecase.LoginUsecase
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val loginUsecase by lazy {
        LoginUsecase()
    }

    private val error: MutableLiveData<Boolean> = MutableLiveData(false)
    private val home: MutableLiveData<Boolean> = MutableLiveData(false)
    val shouldShowError: LiveData<Boolean> = error
    val shouldShowHome: LiveData<Boolean> = home

    fun login(email: String?, password: String?) {
        viewModelScope.launch {
            if (email != null && password != null) {
                val user = loginUsecase.login(email = email, password = password)
                Log.d("Login: ",user.get().toString())
                if (user.get() != null) {
                    home.value = true
                } else {
                    error.value = true
                }
            } else {
                error.value = true
            }
        }
    }
}