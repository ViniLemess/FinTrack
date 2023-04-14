package org.fundatec.vinilemess.tcc.fintrack.balance.service

import org.fundatec.vinilemess.tcc.fintrack.balance.repository.BalanceRepository
import org.fundatec.vinilemess.tcc.fintrack.transaction.service.TransactionService
import org.springframework.stereotype.Service

@Service
class BalanceService(private val transactionService: TransactionService, private val balanceRepository: BalanceRepository) {


}