package org.fundatec.vinilemess.tcc.personalbank.infra

import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class GlobalExceptionHandler: ResponseEntityExceptionHandler()