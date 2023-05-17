package org.fundatec.vinilemess.tcc.fintrackapp.data.remote.api

import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.response.BalanceResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BALANCE_PATH = "/balances/{userSignature}"

interface BalanceApi {

    @GET(BALANCE_PATH)
    suspend fun findBalance(
        @Path("userSignature") userSignature: String,
        @Query("date") date: String?
    ): BalanceResponse
}