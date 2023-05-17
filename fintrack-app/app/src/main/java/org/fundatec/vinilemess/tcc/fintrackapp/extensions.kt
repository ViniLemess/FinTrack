package org.fundatec.vinilemess.tcc.fintrackapp

import org.fundatec.vinilemess.tcc.fintrackapp.data.Transaction
import org.fundatec.vinilemess.tcc.fintrackapp.data.TransactionOperation
import org.fundatec.vinilemess.tcc.fintrackapp.data.User
import org.fundatec.vinilemess.tcc.fintrackapp.data.local.entity.UserEntity
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.response.TransactionResponse
import org.fundatec.vinilemess.tcc.fintrackapp.data.remote.response.UserResponse
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun Double.toCurrency(): String {
    if (this == 0.0) {
        return "$0.00"
    }

    val formatSymbols = DecimalFormatSymbols().apply {
        groupingSeparator = ','
        decimalSeparator = '.'
    }
    val format = DecimalFormat("$#,##0.00", formatSymbols)
    return format.format(this)
}

fun TransactionResponse.toModel(): Transaction {
    return Transaction(
        id = id,
        description = description,
        amount = amount,
        transactionOperation = TransactionOperation.valueOf(transactionOperation),
        date = date.format(DateTimeFormatter.ISO_DATE)
    )
}

fun User.toEntity() = UserEntity(
    id = 1,
    name = name,
    userSignature = userSignature
)

fun UserEntity.toModel() = User(
    name = name,
    userSignature = userSignature
)

fun UserResponse.toModel() = User(
    name = name,
    userSignature = userSignature
)

fun getCurrentDateAsString(): String {
    return LocalDate.now().format(DateTimeFormatter.ISO_DATE).toString()
}







