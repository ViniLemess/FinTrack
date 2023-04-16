package org.fundatec.vinilemess.tcc.fintrack.user.service

import org.fundatec.vinilemess.tcc.fintrack.infra.exception.EmailTakenException
import org.fundatec.vinilemess.tcc.fintrack.infra.exception.UnauthorizedException
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.enums.TransactionOperation
import org.fundatec.vinilemess.tcc.fintrack.transaction.domain.request.TransactionRequest
import org.fundatec.vinilemess.tcc.fintrack.transaction.service.TransactionService
import org.fundatec.vinilemess.tcc.fintrack.user.domain.User
import org.fundatec.vinilemess.tcc.fintrack.user.domain.request.LoginRequest
import org.fundatec.vinilemess.tcc.fintrack.user.domain.request.UserRequest
import org.fundatec.vinilemess.tcc.fintrack.user.domain.response.UserResponse
import org.fundatec.vinilemess.tcc.fintrack.user.repository.UserRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@Service
class UserService(private val userRepository: UserRepository, private val transactionService: TransactionService) {

    fun registerUser(newUser: UserRequest): UserResponse {
        isEmailTaken(newUser.email)
        val user = newUser.buildUser()
        transactionService.transact(buildFirstTransaction(newUser.initialBalance), user.transactionSignature)
        return userRepository.save(user).buildResponse()
    }

    fun login(loginRequest: LoginRequest): UserResponse {
        return userRepository.findByCredentials(loginRequest.email, loginRequest.password)?.buildResponse()
            ?: throw UnauthorizedException("Password or email incorrects")
    }

    private fun isEmailTaken(email: String) {
        val user = userRepository.findByEmail(email)
        if (user != null) {
            throw EmailTakenException("$email has already been taken")
        }
    }

    private fun buildFirstTransaction(initialBalance: BigDecimal): TransactionRequest {
        return TransactionRequest(
            amount = initialBalance,
            date = LocalDate.now(),
            operation = defineOperation(initialBalance),
            description = "First transaction with initial balance amount $$initialBalance"
        )
    }

    private fun defineOperation(initialBalance: BigDecimal) =
        if (initialBalance >= BigDecimal.ZERO)
            TransactionOperation.INCOME
        else TransactionOperation.EXPENSE


    private fun UserRequest.buildUser() = User(
        id = null,
        name = this.name,
        email = this.email,
        password = this.password,
        transactionSignature = UUID.randomUUID().toString()
    )

    private fun User.buildResponse() = UserResponse(this.name, this.email, this.transactionSignature)
}