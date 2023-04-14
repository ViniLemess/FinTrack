package org.fundatec.vinilemess.tcc.fintrack.transaction.controller

import org.fundatec.vinilemess.tcc.fintrack.balance.domain.response.BalanceResponse
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.request.TransactionExpense
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.request.TransactionIncome
import org.fundatec.vinilemess.tcc.fintrack.transaction.service.TransactionService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/transactions")
class TransactionController(private val transactionService: TransactionService) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/income/{userSignature}")
    fun transactIncome(
            @RequestBody transactionIncome: TransactionIncome,
            @PathVariable("userSignature") userSignature: String
    ) {
        transactionIncome.validateRequest()
        transactionService.transactIncome(transactionIncome, userSignature)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/expense/{userSignature}")
    fun transactExpense(
            @RequestBody transactionExpense: TransactionExpense,
            @PathVariable("userSignature") userSignature: String,
    ) {
        transactionExpense.validateRequest()
        transactionService.transactExpense(transactionExpense, userSignature)
    }

    @GetMapping("/balance/{userSignature}")
    fun calculateBalanceForDate(
            @PathVariable("userSignature")
            userSignature: String,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @RequestParam("date")
            date: LocalDate): BalanceResponse {
        return transactionService.calculateBalanceForDate(userSignature, date)
    }
}