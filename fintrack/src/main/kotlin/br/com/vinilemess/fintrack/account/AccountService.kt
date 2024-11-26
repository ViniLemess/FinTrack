package br.com.vinilemess.fintrack.account

import br.com.vinilemess.fintrack.common.ApiResult
import br.com.vinilemess.fintrack.common.ApiResult.Success

class AccountService(private val accountRepository: AccountRepository) {

    suspend fun saveAccount(createAccountRequest: CreateAccountRequest): ApiResult<AccountInfo> {
        return Success(accountRepository.saveAccount(createAccountRequest))
    }
}