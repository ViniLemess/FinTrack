package org.fundatec.vinilemess.tcc.fintrackapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.fundatec.vinilemess.tcc.fintrackapp.data.User
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.response.BalanceResponse
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

    private val username: MutableLiveData<String> = MutableLiveData()
    val showUsername: LiveData<String> = username
    private val balance: MutableLiveData<BalanceResponse> = MutableLiveData()
    val showBalance: LiveData<BalanceResponse> = balance
    private val balanceProjection: MutableLiveData<BalanceResponse> = MutableLiveData()
    val showBalanceProjection: LiveData<BalanceResponse> = balanceProjection

    fun getUsername() {
        viewModelScope.launch {
            username.value = getUser()?.name
        }
    }

    fun getBalance() {
        viewModelScope.launch {
            balance.value = getUser()?.let {
                balanceUseCase.findBalanceByUserSignature(it.userSignature, null)
            }
        }
    }

    fun getBalanceProjection(date: String) {
        viewModelScope.launch {
            balanceProjection.value = getUser()?.let {
                balanceUseCase.findBalanceByUserSignature(it.userSignature, date)
            }
        }
    }

    private suspend fun getUser(): User? {
        return userUseCase.findUser()
    }
}