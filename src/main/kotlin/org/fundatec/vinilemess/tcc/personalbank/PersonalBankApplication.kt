package org.fundatec.vinilemess.tcc.personalbank

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@SpringBootApplication
@EnableWebMvc
class PersonalBankApplication

fun main(args: Array<String>) {
    runApplication<PersonalBankApplication>(*args)
}
