package org.fundatec.vinilemess.tcc.fintrack.transaction.controller

import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.request.RecurrentTransactionRequest
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.request.TransactionRequest
import org.fundatec.vinilemess.tcc.fintrack.transaction.service.TransactionService
import org.fundatec.vinilemess.tcc.fintrack.validation.validateUserSignature
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/transactions")
class TransactionController(private val transactionService: TransactionService) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userSignature}")
    fun transact(
            @RequestBody transactionIncome: TransactionRequest,
            @PathVariable("userSignature") userSignature: String
    ) {
        validateUserSignature(userSignature)
        transactionIncome.validateRequest()
        transactionService.transact(transactionIncome, userSignature)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("recurrent/{userSignature}")
    fun transactRecurrence(
            @RequestBody recurrentTransactionRequest: RecurrentTransactionRequest,
            @PathVariable("userSignature") userSignature: String
    ) {
        validateUserSignature(userSignature)
        recurrentTransactionRequest.validateRequest()
        transactionService.transactRecurrence(recurrentTransactionRequest, userSignature)
    }
}