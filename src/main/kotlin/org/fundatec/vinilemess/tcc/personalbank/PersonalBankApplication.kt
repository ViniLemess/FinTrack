package org.fundatec.vinilemess.tcc.personalbank

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PersonalBankApplication

fun main(args: Array<String>) {
    runApplication<PersonalBankApplication>(*args)
}
