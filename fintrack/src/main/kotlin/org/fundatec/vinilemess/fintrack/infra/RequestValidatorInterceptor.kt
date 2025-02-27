package org.fundatec.vinilemess.fintrack.infra

import org.springframework.core.MethodParameter
import org.springframework.http.HttpInputMessage
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter
import java.lang.reflect.Type

@ControllerAdvice
class RequestValidatorInterceptor : RequestBodyAdviceAdapter() {

    /**
     * This method always returns true, meaning that the afterBodyRead
     * validation should be aplied to all requests that have a body.
     */
    override fun supports(
        methodParameter: MethodParameter,
        targetType: Type,
        converterType: Class<out HttpMessageConverter<*>>
    ) = true

    override fun afterBodyRead(
        body: Any,
        inputMessage: HttpInputMessage,
        parameter: MethodParameter,
        targetType: Type,
        converterType: Class<out HttpMessageConverter<*>>
    ): Any {
        if (body is ValidatableRequest) {
            body.validateRequest()
        }
        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType)
    }
}