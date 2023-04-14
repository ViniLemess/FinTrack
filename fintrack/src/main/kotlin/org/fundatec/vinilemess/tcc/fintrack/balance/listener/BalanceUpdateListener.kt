package org.fundatec.vinilemess.tcc.fintrack.balance.listener

import org.fundatec.vinilemess.tcc.fintrack.balance.domain.Balance
import org.fundatec.vinilemess.tcc.fintrack.balance.domain.response.BalanceResponse
import org.fundatec.vinilemess.tcc.fintrack.balance.repository.BalanceRepository
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.Transaction
import org.fundatec.vinilemess.tcc.fintrack.transaction.service.TransactionService
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class BalanceUpdateListener(
        private val transactionService: TransactionService,
        private val balanceRepository: BalanceRepository
) : AbstractMongoEventListener<Transaction>() {

    override fun onAfterSave(event: AfterSaveEvent<Transaction>) {
        updateBalanceForTransaction(event.source)
    }

    private fun updateBalanceForTransaction(transaction: Transaction) {
        val userSignature = transaction.userSignature
        val currentBalance = transactionService.calculateBalanceForDate(userSignature, LocalDate.now())
        balanceRepository.upsert(currentBalance.toEntity(userSignature))
    }

    private fun BalanceResponse.toEntity(userSignature: String) = Balance(
            id = null,
            userSignature = userSignature,
            amount = amount,
            date = LocalDate.now()
    )
}