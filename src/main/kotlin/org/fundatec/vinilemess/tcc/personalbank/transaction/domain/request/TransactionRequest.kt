package org.fundatec.vinilemess.tcc.personalbank.transaction.domain.request

import org.fundatec.vinilemess.tcc.personalbank.transaction.domain.TransactionOperation
import java.math.BigDecimal
import java.time.LocalDate

interface TransactionRequest {
    val amount: BigDecimal
    val date: LocalDate
    val operation: TransactionOperation
    fun validateRequest()
}