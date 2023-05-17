package org.fundatec.vinilemess.tcc.fintrackapp.data.remote.datasource

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.RetrofitNetworkClient
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.api.BalanceApi
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.response.BalanceResponse
import org.fundatec.vinilemess.tcc.fintrackapp.getCurrentDateAsString

class BalanceDataSource {
    private val client = RetrofitNetworkClient.createNetworkClient()
        .create(BalanceApi::class.java)

    suspend fun findBalance(userSignature: String, date: String?): BalanceResponse {
        return withContext(Dispatchers.IO) {
            try {
                client.findBalance(userSignature, date?: getCurrentDateAsString())
            } catch (exception: Exception) {
                Log.e("msg: {}, error is {}", exception.message, exception)
                BalanceResponse(0.0, date?: getCurrentDateAsString())
            }
        }
    }
}