package org.fundatec.vinilemess.fintrack.transaction.domain.request

import org.fundatec.vinilemess.fintrack.transaction.domain.enums.TransactionOperation
import org.fundatec.vinilemess.fintrack.validation.DataValidator
import org.springframework.format.annotation.DateTimeFormat
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

data class TransactionRequest(
    private var amount: BigDecimal,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    val date: LocalDate,
    val description: String?,
    val operation: TransactionOperation
) : org.fundatec.vinilemess.fintrack.infra.ValidatableRequest {

    init {
        this.amount = negateIfExpense(amount)
    }

    override fun validateRequest() {
        DataValidator()
            .addCustomConstraint(isAmountZero(), "amount", "amount cannot be 0")
            .addNotNullConstraint(operation, "operation")
            .validate()
    }

    private fun isAmountZero(): () -> Boolean = { Objects.isNull(amount) || amount == BigDecimal.ZERO }

    private fun negateIfExpense(amount: BigDecimal): BigDecimal {
        if (operation == TransactionOperation.EXPENSE) {
            return amount.negate()
        }
        return amount
    }

    fun getAmount(): BigDecimal {
        return this.amount
    }
}