package org.fundatec.vinilemess.fintrack.security.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CORSConfiguration {

    @Bean
    fun corsConfiguration() = object : WebMvcConfigurer {
        override fun addCorsMappings(registry: CorsRegistry) {
            registry.addMapping("/**")
                    .allowedMethods("GET", "POST", "PUT", "DELETE")
                    .allowedOrigins(
                            "http://127.0.0.1:8080",
                            "http://localhost:8080",
                            "https://localhost:3000",
                            "http://localhost:3000"
                    )
                    .exposedHeaders("Set-Cookie")
                    .allowCredentials(true)
        }
    }
}