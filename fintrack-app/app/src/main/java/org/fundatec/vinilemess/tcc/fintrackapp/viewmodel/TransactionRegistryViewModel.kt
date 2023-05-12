package org.fundatec.vinilemess.tcc.fintrackapp.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.fundatec.vinilemess.tcc.fintrackapp.usecase.TransactionUsecase
import kotlinx.coroutines.launch
import org.fundatec.vinilemess.tcc.fintrackapp.data.TransactionOperation
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.request.TransactionRequest
import org.fundatec.vinilemess.tcc.fintrackapp.getCurrentDateAsString
import org.fundatec.vinilemess.tcc.fintrackapp.usecase.UserUseCase
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TransactionRegistryViewModel(context: Context) : ViewModel() {
    private val usecase by lazy {
        TransactionUsecase()
    }
    private val userUseCase by lazy {
        UserUseCase(context)
    }
    private val state: MutableLiveData<TransactionState> = MutableLiveData()
    val viewState: LiveData<TransactionState> = state

    fun saveTransaction(operation: TransactionOperation, amount: String, description: String, date: String) {
        viewModelScope.launch {
            if (amount.isBlank()) {
                state.value = TransactionState.EmptyTransaction
                return@launch
            } else if (validateTransaction(operation)) {
                state.value = TransactionState.UnknownType
                return@launch
            }
            val newTransaction = TransactionRequest(
                description = description,
                amount = amount.trim().toDouble(),
                date = date,
                operation = operation.toString()
            )
            Log.i("objeto ====>", newTransaction.toString())
            userUseCase.findUser()
                ?.let { usecase.saveTransaction(newTransaction, it.userSignature) }
            state.value = TransactionState.ValidTransaction
        }
    }

    private fun validateTransaction(operation: TransactionOperation): Boolean {
        return operation == TransactionOperation.UNKNOWN
    }
}

sealed class TransactionState() {
    object EmptyTransaction : TransactionState()
    object UnknownType : TransactionState()
    object ValidTransaction : TransactionState()
}