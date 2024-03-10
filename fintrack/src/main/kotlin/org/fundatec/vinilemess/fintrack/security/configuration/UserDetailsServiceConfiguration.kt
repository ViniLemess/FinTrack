package org.fundatec.vinilemess.fintrack.security.configuration

import org.fundatec.vinilemess.fintrack.user.repository.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

@Configuration
class UserDetailsServiceConfiguration {

    @Bean
    fun userDetailsService(userRepository: UserRepository): UserDetailsService {
        return UserDetailsService { username ->
            userRepository.findByEmail(username)
                ?: throw UsernameNotFoundException("No user found with the $username email.")
        }
    }
}