package org.fundatec.vinilemess.tcc.fintrackapp.data.remote.datasource

import android.util.Log
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.response.TransactionResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.RetrofitNetworkClient
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.api.TransactionsApi
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.request.TransactionRequest

class TransactionDataSource {
    private val client = RetrofitNetworkClient.createNetworkClient()
        .create(TransactionsApi::class.java)

    suspend fun registerTransaction(transaction: TransactionRequest) {
        withContext(Dispatchers.IO) {
            try {
                client.registerTransaction("", transaction)
            } catch (exception:Exception) {
                Log.e("msg: {}, error is {}", exception.message, exception)
            }
        }
    }

    suspend fun findTransactions(userSignature: String): List<TransactionResponse> {
        return withContext(Dispatchers.IO) {
            try {
                client.findTransactions(userSignature, null)
            } catch (exception:Exception) {
                Log.e("msg: {}, error is {}", exception.message, exception)
                emptyList()
            }
        }
    }

    suspend fun deleteTransaction(id:String) {
        withContext(Dispatchers.IO) {
            try {
                client.deleteTransaction(id)
            } catch  (exception:Exception) {
                Log.e("msg: {}, error is {}", exception.message, exception)
            }
        }
    }
}