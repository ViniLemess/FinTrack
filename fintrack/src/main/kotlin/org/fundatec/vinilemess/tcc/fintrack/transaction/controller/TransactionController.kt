package org.fundatec.vinilemess.tcc.fintrack.transaction.controller

import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.TransactionResponse
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.request.RecurrentTransactionRequest
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.request.TransactionRequest
import org.fundatec.vinilemess.tcc.fintrack.transaction.service.TransactionService
import org.fundatec.vinilemess.tcc.fintrack.validation.UserSignature
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
            @PathVariable("userSignature") userSignature: UserSignature,
            @RequestParam("date") date: LocalDate?
    ): ResponseEntity<List<TransactionResponse>> {
        return ResponseEntity.ok(
                transactionService.listTransactionsBeforeDateByUserSignature(
                        userSignature.userSignature,
                        date ?: LocalDate.now()
                )
        )
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userSignature}")
    fun transact(
            @RequestBody transactionRequest: TransactionRequest,
            @PathVariable("userSignature") userSignature: UserSignature
    ) {
        transactionService.transact(transactionRequest, userSignature.userSignature)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/recurrent/{userSignature}")
    fun transactRecurrence(
            @RequestBody recurrentTransactionRequest: RecurrentTransactionRequest,
            @PathVariable("userSignature") userSignature: UserSignature
    ) {
        transactionService.transactRecurrence(recurrentTransactionRequest, userSignature.userSignature)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun deleteTransactionById(@PathVariable("id") id: String) {
        transactionService.deleteTransactionById(id)
    }
}