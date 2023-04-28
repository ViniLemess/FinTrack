package org.fundatec.vinilemess.tcc.fintrack.transaction.controller

import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.TransactionResponse
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.request.RecurrentTransactionRequest
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.request.TransactionRequest
import org.fundatec.vinilemess.tcc.fintrack.transaction.service.TransactionService
import org.fundatec.vinilemess.tcc.fintrack.validation.validateUserSignature
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/transactions")
class TransactionController(private val transactionService: TransactionService) {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userSignature}")
    fun getTransactions(
        @PathVariable("userSignature") userSignature: String,
        @RequestParam("date") date: LocalDate?
    ): ResponseEntity<List<TransactionResponse>> {
        validateUserSignature(userSignature)
        return ResponseEntity.ok(
            transactionService.listTransactionsBeforeDateByUserSignature(
                userSignature,
                date ?: LocalDate.now()
            )
        )
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userSignature}")
    fun transact(
        @RequestBody transactionRequest: TransactionRequest,
        @PathVariable("userSignature") userSignature: String
    ) {
        validateUserSignature(userSignature)
        transactionService.transact(transactionRequest, userSignature)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("recurrent/{userSignature}")
    fun transactRecurrence(
        @RequestBody recurrentTransactionRequest: RecurrentTransactionRequest,
        @PathVariable("userSignature") userSignature: String
    ) {
        validateUserSignature(userSignature)
        transactionService.transactRecurrence(recurrentTransactionRequest, userSignature)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun deleteTransactionById(@PathVariable("id") id: String) {
        transactionService.deleteTransactionById(id)
    }
}