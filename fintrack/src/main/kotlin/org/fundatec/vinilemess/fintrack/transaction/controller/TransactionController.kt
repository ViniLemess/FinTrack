package org.fundatec.vinilemess.fintrack.transaction.controller

import org.fundatec.vinilemess.fintrack.transaction.domain.TransactionResponse
import org.fundatec.vinilemess.fintrack.transaction.domain.request.RecurrentTransactionRequest
import org.fundatec.vinilemess.fintrack.transaction.domain.request.TransactionRequest
import org.fundatec.vinilemess.fintrack.transaction.service.TransactionService
import org.fundatec.vinilemess.fintrack.user.domain.UserSignature
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/transactions")
class TransactionController(private val transactionService: TransactionService) {

    @GetMapping("/{userSignature}")
    fun getTransactions(@PathVariable("userSignature") userSignature: UserSignature,
                        @RequestParam("date") date: LocalDate?): ResponseEntity<List<TransactionResponse>> {
        return ResponseEntity.ok(
                transactionService.listTransactionsBeforeDateByUserSignature(
                        userSignature.userSignature,
                        date ?: LocalDate.now()
                )
        )
    }

    @PostMapping("/{userSignature}")
    fun transact(@RequestBody transactionRequest: TransactionRequest,
                 @PathVariable("userSignature") userSignature: UserSignature): ResponseEntity<Unit> {
        transactionService.transact(transactionRequest, userSignature.userSignature)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/recurrent/{userSignature}")
    fun transactRecurrence(@RequestBody recurrentTransactionRequest: RecurrentTransactionRequest,
                           @PathVariable("userSignature") userSignature: UserSignature): ResponseEntity<Unit> {
        transactionService.transactRecurrence(recurrentTransactionRequest, userSignature.userSignature)
        return ResponseEntity.status(HttpStatus.CREATED).build()

    }

    @DeleteMapping
    fun deleteTransactionsByIdList(@RequestParam("id") idList: List<String>): ResponseEntity<Unit> {
        transactionService.deleteTransactionsByIdList(idList)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}