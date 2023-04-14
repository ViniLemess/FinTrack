package org.fundatec.vinilemess.tcc.fintrack.balance.domain.response

import java.math.BigDecimal
import java.time.LocalDate

data class BalanceResponse(val amount: BigDecimal, val date: LocalDate)
