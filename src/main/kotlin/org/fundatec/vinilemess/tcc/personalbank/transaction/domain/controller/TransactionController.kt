package org.fundatec.vinilemess.tcc.personalbank.transaction.domain.controller

import jakarta.validation.Valid
import org.fundatec.vinilemess.tcc.personalbank.transaction.domain.input.TransactionExpense
import org.fundatec.vinilemess.tcc.personalbank.transaction.domain.input.TransactionIncome
import org.fundatec.vinilemess.tcc.personalbank.transaction.service.TransactionService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/transaction")
class TransactionController(private val transactionService: TransactionService) {

    @PostMapping("/income")
    fun transactIncome(@Valid @RequestBody transactionIncome: TransactionIncome) {
        transactionService.transactIncome(transactionIncome)
    }

    @PostMapping("/expense")
    fun transactExpense(@Valid @RequestBody transactionExpense: TransactionExpense) {
        transactionService.transactExpense(transactionExpense)
    }
}