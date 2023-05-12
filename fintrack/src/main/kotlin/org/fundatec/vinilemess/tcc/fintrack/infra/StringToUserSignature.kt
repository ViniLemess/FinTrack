package org.fundatec.vinilemess.tcc.fintrack.infra

import org.fundatec.vinilemess.tcc.fintrack.user.domain.UserSignature
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class StringToUserSignature: Converter<String, UserSignature> {
    override fun convert(source: String): UserSignature {
        return UserSignature(source)
    }
}