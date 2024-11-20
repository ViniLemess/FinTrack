package br.com.vinilemess.fintrack.common.database

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.math.BigDecimal

class CustomColumns {
    companion object {
        private const val PRECISION = 18
        private const val SCALE = 2

        fun Table.money(name: String): Column<BigDecimal> {
            return decimal(name, PRECISION, SCALE)
        }
    }
}