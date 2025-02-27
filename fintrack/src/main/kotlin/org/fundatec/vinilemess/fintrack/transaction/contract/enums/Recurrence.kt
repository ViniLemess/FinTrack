package org.fundatec.vinilemess.fintrack.transaction.contract.enums

import java.time.LocalDate

enum class Recurrence {
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY;

    fun calculateDateByFrequency(date: LocalDate, frequency: Long): LocalDate {
        return when (this) {
            DAILY -> date.plusDays(frequency)
            WEEKLY -> date.plusWeeks(frequency)
            MONTHLY -> date.plusMonths(frequency)
            YEARLY -> date.plusYears(frequency)
        }
    }
}