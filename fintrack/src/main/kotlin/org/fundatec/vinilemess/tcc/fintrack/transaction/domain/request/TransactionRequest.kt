package org.fundatec.vinilemess.tcc.fintrack.transaction.domain.request

import org.fundatec.vinilemess.tcc.fintrack.infra.Request
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.enums.TransactionOperation
import org.fundatec.vinilemess.tcc.fintrack.validation.DataValidator
import org.springframework.format.annotation.DateTimeFormat
import java.math.BigDecimal
import java.time.LocalDate
import java.util.Objects

data class TransactionRequest(
    val amount: BigDecimal,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    val date: LocalDate,
    val description: String?,
    val operation: TransactionOperation
): Request {
    override fun validateRequest() {
        DataValidator()
            .addCustomConstraint(isAmountZero(), "amount", "amount cannot be 0")
            .addNotNullConstraint(operation, "operation")
            .validate()
    }
    private fun isAmountZero() = { -> Objects.isNull(amount) || amount == BigDecimal.ZERO }
}