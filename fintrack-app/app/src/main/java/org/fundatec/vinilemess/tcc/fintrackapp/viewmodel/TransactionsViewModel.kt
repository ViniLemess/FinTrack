package org.fundatec.vinilemess.tcc.fintrackapp.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.fundatec.vinilemess.tcc.fintrackapp.data.Transaction
import org.fundatec.vinilemess.tcc.fintrackapp.usecase.TransactionUsecase
import org.fundatec.vinilemess.tcc.fintrackapp.usecase.UserUseCase

class TransactionsViewModel(context: Context): ViewModel() {
    private val transactionUsecase by lazy {
        TransactionUsecase()
    }
    private val userUseCase by lazy {
        UserUseCase(context)
    }

    private val transactions: MutableLiveData<List<Transaction>> = MutableLiveData()
    val listTransactions: LiveData<List<Transaction>> = transactions

    fun getTransactions(date: String?) {
        viewModelScope.launch {
            val list = findTransactions(date)?: listOf()
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

    private suspend fun findTransactions(date: String?): List<Transaction>? {
        return userUseCase.findUser()?.let { transactionUsecase.findTransactions(it.userSignature, date = date) }
    }
}