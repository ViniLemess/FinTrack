package org.fundatec.vinilemess.tcc.fintrackapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.fundatec.vinilemess.tcc.fintrackapp.toCurrency
import org.fundatec.vinilemess.tcc.fintrackapp.usecase.TransactionUsecase
import kotlinx.coroutines.launch
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.response.BalanceResponse

class HomeViewModel: ViewModel(){
    private val transactionUsecase by lazy {
        TransactionUsecase()
    }

    private val balance: MutableLiveData<String> = MutableLiveData()
    val showBill: LiveData<String> = balance

    fun getTotalBill() {
        viewModelScope.launch {
            balance.value = 0.0.toCurrency()
        }
    }
}