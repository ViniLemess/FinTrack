package org.fundatec.vinilemess.fintrack.balance

import java.math.BigDecimal
import java.time.LocalDate

data class BalanceResponse(val amount: BigDecimal, val date: LocalDate)