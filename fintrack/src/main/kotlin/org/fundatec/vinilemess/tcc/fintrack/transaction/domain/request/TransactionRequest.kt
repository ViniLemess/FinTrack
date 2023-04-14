package org.fundatec.vinilemess.tcc.fintrack.transaction.domain.request

import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.TransactionOperation
import java.math.BigDecimal
import java.time.LocalDate

interface TransactionRequest {
    val amount: BigDecimal
    val date: LocalDate
    val description: String?
    val operation: TransactionOperation
    fun validateRequest()
}