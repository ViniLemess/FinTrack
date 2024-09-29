package org.fundatec.vinilemess.fintrack.transaction

import org.fundatec.vinilemess.fintrack.transaction.contract.TransactionResponse
import org.fundatec.vinilemess.fintrack.transaction.contract.request.RecurrentTransactionRequest
import org.fundatec.vinilemess.fintrack.transaction.contract.request.TransactionRequest
import org.fundatec.vinilemess.fintrack.user.contract.UserSignature
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/transactions")
class TransactionController(private val transactionService: TransactionService) {

    @GetMapping("/{userSignature}")
    @PreAuthorize("hasAnyRole('USER')")
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

    @PostMapping("/{userSignature}")
    @PreAuthorize("hasAnyRole('USER')")
    fun transact(
        @RequestBody transactionRequest: TransactionRequest,
        @PathVariable("userSignature") userSignature: UserSignature
    ): ResponseEntity<Unit> {
        transactionService.transact(transactionRequest, userSignature.userSignature)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/recurrent/{userSignature}")
    @PreAuthorize("hasAnyRole('USER')")
    fun transactRecurrence(
        @RequestBody recurrentTransactionRequest: RecurrentTransactionRequest,
        @PathVariable("userSignature") userSignature: UserSignature
    ): ResponseEntity<Unit> {
        transactionService.transactRecurrence(recurrentTransactionRequest, userSignature.userSignature)
        return ResponseEntity.status(HttpStatus.CREATED).build()

    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('USER')")
    fun deleteTransactionsByIdList(@RequestParam("id") idList: List<String>): ResponseEntity<Unit> {
        transactionService.deleteTransactionsByIdList(idList)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}