package org.fundatec.vinilemess.tcc.fintrackapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.fundatec.vinilemess.tcc.fintrackapp.data.Transaction
import org.fundatec.vinilemess.tcc.fintrackapp.usecase.TransactionUsecase

class TransactionsViewModel: ViewModel() {
    private val transactionUsecase by lazy {
        TransactionUsecase()
    }

    private val transactions: MutableLiveData<List<Transaction>> = MutableLiveData()
    val listTransactions: LiveData<List<Transaction>> = transactions

    fun findTransactions(userSignature: String) {
        viewModelScope.launch {
            val list = transactionUsecase.findTransactions(userSignature)
            if (list.isEmpty()){
                Log.e("TransactionsViewModel", "vazio")
            } else {
                transactions.value = list
                list.forEach {
                    Log.e("TransactionList ===>", it.date)
                }
            }
        }
    }
}