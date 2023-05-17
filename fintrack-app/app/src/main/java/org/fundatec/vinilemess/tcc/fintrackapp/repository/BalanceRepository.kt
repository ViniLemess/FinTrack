package org.fundatec.vinilemess.tcc.fintrackapp.repository

import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.datasource.BalanceDataSource
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.response.BalanceResponse

class BalanceRepository {
    private val balanceDataSource by lazy {
        BalanceDataSource()
    }

    suspend fun findBalanceByUserSignature(userSignature: String, date: String?): BalanceResponse {
        return balanceDataSource.findBalance(userSignature, date)
    }
}