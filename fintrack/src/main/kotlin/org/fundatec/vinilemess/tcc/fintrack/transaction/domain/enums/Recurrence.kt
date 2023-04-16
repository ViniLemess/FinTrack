package org.fundatec.vinilemess.tcc.fintrack.transaction.domain.enums

import java.time.LocalDate

enum class Recurrence {
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY;

    fun calculateDateByFrequency(initialDate: LocalDate, frequency: Long): LocalDate {
        return when (this) {
            DAILY -> initialDate.plusDays(frequency)
            WEEKLY -> initialDate.plusWeeks(frequency)
            MONTHLY -> initialDate.plusMonths(frequency)
            YEARLY -> initialDate.plusYears(frequency)
        }
    }
}