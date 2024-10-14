package br.com.vinilemess.fintrack.error

class ErrorResponseException(val problemDetail: ProblemDetail): RuntimeException(problemDetail.detail)