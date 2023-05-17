package org.fundatec.vinilemess.tcc.fintrackapp.data.remote.api

import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.request.RecurrentTransactionRequest
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.request.TransactionRequest
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.response.TransactionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

private const val TRANSACTIONS_PATH = "/transactions"
private const val USER_SIGNATURE_PATH_VARIABLE = "{userSignature}"

interface TransactionsApi {

    @POST("$TRANSACTIONS_PATH/$USER_SIGNATURE_PATH_VARIABLE")
    suspend fun registerTransaction(
        @Path("userSignature") userSignature: String,
        @Body transactionRequest: TransactionRequest
    )

    @POST("$TRANSACTIONS_PATH/recurrent/$USER_SIGNATURE_PATH_VARIABLE")
    suspend fun registerRecurrentTransaction(
        @Path("userSignature") userSignature: String,
        @Body recurrentTransactionRequest: RecurrentTransactionRequest
    )

    @GET("$TRANSACTIONS_PATH/$USER_SIGNATURE_PATH_VARIABLE")
    suspend fun findTransactions(
        @Path("userSignature") userSignature: String,
        @Query("date") date: String?
    ): List<TransactionResponse>

    @DELETE(TRANSACTIONS_PATH)
    suspend fun deleteTransaction(@Query("id") id: List<String>): Response<Unit>
}