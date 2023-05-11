package org.fundatec.vinilemess.tcc.fintrackapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.fundatec.vinilemess.tcc.fintrackapp.usecase.LoginUsecase
import kotlinx.coroutines.launch
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.response.UserResponse

class LoginViewModel : ViewModel() {

    private val loginUsecase by lazy {
        LoginUsecase()
    }

    private val error: MutableLiveData<Boolean> = MutableLiveData(false)
    private val home: MutableLiveData<Boolean> = MutableLiveData(false)
    private val mutableUserSignature: MutableLiveData<String> = MutableLiveData("")
    val userSignature: LiveData<String> = mutableUserSignature
    val shouldShowError: LiveData<Boolean> = error
    val shouldShowHome: LiveData<Boolean> = home

    fun login(email: String?, password: String?) {
        viewModelScope.launch {
            if (email != null && password != null) {
                val user = loginUsecase.login(email = email, password = password)
                Log.d("Login: ",user.get().toString())
                val userResponse: UserResponse? = user.get()
                if (userResponse != null) {
                    mutableUserSignature.value = userResponse.userSignature
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