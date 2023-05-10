package org.fundatec.vinilemess.tcc.fintrackapp

import org.fundatec.vinilemess.tcc.fintrackapp.data.Transaction
import org.fundatec.vinilemess.tcc.fintrackapp.data.TransactionOperation
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.response.TransactionResponse
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun Double.toCurrency(): String {
    if (this == 0.0) {
        return "R$ 0,00"
    }
    val format = DecimalFormat("R$ #,###.##")
    format.isDecimalSeparatorAlwaysShown = false
    return format.format(this).toString()
}

fun TransactionResponse.toModel(): Transaction {
    return Transaction(
        description = description,
        amount = amount.toDouble(),
        transactionOperation = TransactionOperation.valueOf(transactionOperation),
        date = date.format(DateTimeFormatter.ISO_DATE)
    )
}

fun getCurrentDateAsString(): String {
    return LocalDate.now().format(DateTimeFormatter.ISO_DATE).toString()
}





