package org.fundatec.vinilemess.fintrack.balance.controller

import org.fundatec.vinilemess.fintrack.balance.domain.BalanceResponse
import org.fundatec.vinilemess.fintrack.balance.service.BalanceService
import org.fundatec.vinilemess.fintrack.user.domain.UserSignature
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/balances")
class BalanceController(private val balanceService: BalanceService) {

    @GetMapping("/{userSignature}")
    fun getUserBalanceForDate(
        @PathVariable("userSignature") userSignature: UserSignature,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam("date") date: LocalDate?
    ): ResponseEntity<BalanceResponse> {
        return ResponseEntity.ok(
            balanceService.calculateBalanceForDate(
                userSignature.userSignature,
                date ?: LocalDate.now()
            )
        )
    }
}