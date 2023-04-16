package org.fundatec.vinilemess.tcc.fintrack.balance.controller

import org.fundatec.vinilemess.tcc.fintrack.balance.domain.BalanceResponse
import org.fundatec.vinilemess.tcc.fintrack.balance.service.BalanceService
import org.fundatec.vinilemess.tcc.fintrack.validation.validateUserSignature
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("balances")
class BalanceController(private val balanceService: BalanceService) {

    @GetMapping("/user-signature/{userSignature}")
    fun getUserBalanceForDate(
        @PathVariable("userSignature") userSignature: String,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam("date") date: LocalDate?
    ): ResponseEntity<BalanceResponse> {
        validateUserSignature(userSignature)
        return ResponseEntity.ok(balanceService.calculateBalanceForDate(userSignature, date?: LocalDate.now()))
    }
}