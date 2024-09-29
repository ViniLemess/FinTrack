package org.fundatec.vinilemess.fintrack.balance

import org.fundatec.vinilemess.fintrack.user.contract.UserSignature
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/balances")
class BalanceController(private val balanceService: BalanceService) {

    @GetMapping("/{userSignature}")
    @PreAuthorize("hasAnyRole('USER')")
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