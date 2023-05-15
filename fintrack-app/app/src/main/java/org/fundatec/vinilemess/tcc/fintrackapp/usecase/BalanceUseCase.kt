package org.fundatec.vinilemess.tcc.fintrackapp.usecase

import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.response.BalanceResponse
import org.fundatec.vinilemess.tcc.fintrackapp.repository.BalanceRepository

class BalanceUseCase {
    private val balanceRepository by lazy {
        BalanceRepository()
    }

    suspend fun findBalanceByUserSignature(userSignature: String): BalanceResponse {
        return balanceRepository.findBalanceByUserSignature(userSignature)
    }
}