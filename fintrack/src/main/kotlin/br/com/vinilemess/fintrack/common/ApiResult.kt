package br.com.vinilemess.fintrack.common

sealed class ApiResult<out T> {

    data class Success<out T>(val value: T) : ApiResult<T>()

    data class Failure(val problemDetail: ProblemDetail) : ApiResult<Nothing>()

    inline fun onSuccess(action: (T) -> Unit): ApiResult<T> {
        if (this is Success) action(value)
        return this
    }

    inline fun onFailure(action: (ProblemDetail) -> Unit): ApiResult<T> {
        if (this is Failure) action(problemDetail)
        return this
    }
}