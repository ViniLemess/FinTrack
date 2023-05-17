package org.fundatec.vinilemess.tcc.fintrackapp.data.remote.datasource

import android.util.Log
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.response.TransactionResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.RetrofitNetworkClient
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.api.TransactionsApi
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.request.TransactionRequest
import org.fundatec.vinilemess.tcc.fintrackapp.getCurrentDateAsString

class TransactionDataSource {
    private val client = RetrofitNetworkClient.createNetworkClient()
        .create(TransactionsApi::class.java)

    suspend fun registerTransaction(transaction: TransactionRequest, userSignature: String) {
        withContext(Dispatchers.IO) {
            try {
                client.registerTransaction(userSignature, transaction)
            } catch (exception:Exception) {
                Log.e("msg: {}, error is {}", exception.message, exception)
            }
        }
    }

    suspend fun findTransactions(userSignature: String, date: String?): List<TransactionResponse> {
        return withContext(Dispatchers.IO) {
            try {
                client.findTransactions(userSignature, date?: getCurrentDateAsString())
            } catch (exception:Exception) {
                Log.e("msg: {}, error is {}", exception.message, exception)
                emptyList()
            }
        }
    }

    suspend fun deleteTransaction(ids: List<String>) {
        withContext(Dispatchers.IO) {
            try {
                client.deleteTransaction(ids)
            } catch  (exception:Exception) {
                Log.e("msg: {}, error is {}", exception.message, exception)
            }
        }
    }
}