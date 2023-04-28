package org.fundatec.vinilemess.tcc.fintrack

import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

val testDate: LocalDate = LocalDate.of(2023, 4, 13)
val testUserSignature = UUID.randomUUID().toString()
val testPositiveAmount: BigDecimal = BigDecimal.TEN
val testNegativeAmount: BigDecimal = BigDecimal.TEN.negate()
const val testDescription = "test"
val testId = UUID.randomUUID().toString()