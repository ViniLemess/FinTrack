package org.fundatec.vinilemess.fintrack

import org.fundatec.vinilemess.fintrack.user.domain.request.UserRequest
import org.fundatec.vinilemess.fintrack.user.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class FinTrackApplication {

    private val logger = LoggerFactory.getLogger(FinTrackApplication::class.java)

    @Bean
    fun commandLineRunner(userService: UserService): CommandLineRunner {
        return CommandLineRunner {
            try {
                userService.registerUser(
                    UserRequest(
                        name = "John Doe",
                        email = "johndoe@example.com",
                        password = "password123"
                    )
                )
            } catch (e: Exception) {
                logger.warn("Initial user already registered", e)
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<FinTrackApplication>(*args)
}
