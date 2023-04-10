package org.fundatec.vinilemess.tcc.personalbank.transaction.controller

import org.fundatec.vinilemess.tcc.personalbank.transaction.domain.input.TransactionExpense
import org.fundatec.vinilemess.tcc.personalbank.transaction.domain.input.TransactionIncome
import org.fundatec.vinilemess.tcc.personalbank.transaction.service.TransactionService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/transaction")
class TransactionController(private val transactionService: TransactionService) {

    @PostMapping("/income/{userIdentifier}")
    fun transactIncome(
        @RequestBody transactionIncome: TransactionIncome,
        @PathVariable("userIdentifier") userIdentifier: String
    ) {
        transactionService.transactIncome(transactionIncome, userIdentifier)
    }

    @PostMapping("/expense/{userIdentifier}")
    fun transactExpense(
        @RequestBody transactionExpense: TransactionExpense,
        @PathVariable("userIdentifier") userIdentifier: String,
    ) {
        transactionService.transactExpense(transactionExpense, userIdentifier)
    }
}