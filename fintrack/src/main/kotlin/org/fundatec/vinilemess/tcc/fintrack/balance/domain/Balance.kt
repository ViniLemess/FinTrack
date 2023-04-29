package org.fundatec.vinilemess.tcc.fintrack.balance.domain

import java.math.BigDecimal
import java.time.LocalDate

data class BalanceResponse(val amount: BigDecimal, val date: LocalDate)