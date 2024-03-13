package org.fundatec.vinilemess.fintrack.security.configuration

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.fundatec.vinilemess.fintrack.security.JwtAuthenticationFilter
import org.fundatec.vinilemess.fintrack.security.LogoutHandler
import org.fundatec.vinilemess.fintrack.user.domain.enums.Permission
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfiguration(
    private val logoutHandler: LogoutHandler,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val authenticationProvider: AuthenticationProvider,
    private val delegatedAuthenticationEntryPoint: DelegatedAuthenticationEntryPoint
) {

    private val whiteListUrls: Array<String> = arrayOf(
        "/v2/api-docs",
        "/v3/api-docs",
        "/v3/api-docs/**",
        "/swagger-resources",
        "/auths/authenticate",
        "/actuator/health",
        "/swagger-resources/**",
        "/login",
        "/refresh",
        "/configuration/ui",
        "/configuration/security",
        "/swagger-ui/**",
        "/webjars/**",
        "/swagger-ui.html"
    )

    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        return httpSecurity
            .csrf { it.disable() }
            .authorizeHttpRequests { req ->
                req.requestMatchers(*whiteListUrls)
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/users").hasAnyAuthority(
                        Permission.USER_CREATE.getPermission()
                    )
                    .anyRequest()
                    .authenticated()
            }
            .sessionManagement { session: SessionManagementConfigurer<HttpSecurity> ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .logout { logout: LogoutConfigurer<HttpSecurity> ->
                logout.logoutUrl("/api/v1/sessions/logout")
                    .addLogoutHandler(logoutHandler)
                    .logoutSuccessHandler { _: HttpServletRequest?, _: HttpServletResponse?, _: Authentication? ->
                        SecurityContextHolder.clearContext()
                    }
            }
            .httpBasic { it.authenticationEntryPoint(this.delegatedAuthenticationEntryPoint) }
            .exceptionHandling(Customizer.withDefaults())
            .build()
    }
}
