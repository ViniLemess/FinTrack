package org.fundatec.vinilemess.tcc.personalbank.balance

import java.math.BigDecimal
import java.time.LocalDate

data class Balance(val amount: BigDecimal, val date: LocalDate)
