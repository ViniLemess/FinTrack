package org.fundatec.vinilemess.tcc.fintrackapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.fundatec.vinilemess.tcc.fintrackapp.data.User
import org.fundatec.vinilemess.tcc.fintrackapp.toCurrency
import org.fundatec.vinilemess.tcc.fintrackapp.usecase.BalanceUseCase
import org.fundatec.vinilemess.tcc.fintrackapp.usecase.UserUseCase

class HomeViewModel(context: Context): ViewModel(){
    private val balanceUseCase by lazy {
        BalanceUseCase()
    }
    private val userUseCase by lazy {
        UserUseCase(context)
    }

    private val balance: MutableLiveData<String> = MutableLiveData()
    val showBalance: LiveData<String> = balance
    private val username: MutableLiveData<String> = MutableLiveData()
    val showUsername: LiveData<String> = username

    fun getBalance() {
        viewModelScope.launch {
            balance.value = getUser()?.let {
                balanceUseCase.findBalanceByUserSignature(it.userSignature).amount.toCurrency()
            }
        }
    }

    fun getUsername() {
        viewModelScope.launch {
            username.value = getUser()?.let { user -> user.name }
        }
    }

    private suspend fun getUser(): User? {
        return userUseCase.findUser()
    }
}