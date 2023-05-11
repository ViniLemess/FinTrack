package org.fundatec.vinilemess.tcc.fintrackapp.data.remote.api

import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.response.BalanceResponse
import retrofit2.http.GET
import retrofit2.http.Path

private const val BALANCE_PATH = "/balances/{userSignature}"

interface BalanceApi {

    @GET(BALANCE_PATH)
    fun findBalance(@Path("userSignature") userSignature: String): BalanceResponse
}