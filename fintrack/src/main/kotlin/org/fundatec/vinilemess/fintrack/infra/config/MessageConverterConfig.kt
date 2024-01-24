package org.fundatec.vinilemess.fintrack.infra.config

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class MessageConverterConfig : WebMvcConfigurer {
    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>?>) {
        super.configureMessageConverters(converters)
        converters.add(
            MappingJackson2HttpMessageConverter(
                Jackson2ObjectMapperBuilder()
                    .dateFormat(StdDateFormat())
                    .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                    .build()
            )
        )
    }
}
