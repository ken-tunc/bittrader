package org.kentunc.bittrader.web.infrastructure.configuration

import org.kentunc.bittrader.web.infrastructure.configuration.properties.WebSecurityConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.server.SecurityWebFilterChain

@Profile("auth")
@EnableWebFluxSecurity
@EnableConfigurationProperties(WebSecurityConfigurationProperties::class)
class WebSecurityConfiguration(private val properties: WebSecurityConfigurationProperties) {

    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http.csrf().disable()
            .authorizeExchange()
            .pathMatchers(*properties.excludePaths.toTypedArray()).permitAll()
            .anyExchange().authenticated()
            .and()
            .httpBasic()
            .and()
            .formLogin().disable()
            .build()
    }

    @Bean
    fun userDetailsService(): MapReactiveUserDetailsService {
        val user = User.builder()
            .username(properties.username.trim())
            .password("{noop}${properties.password.trim()}")
            .roles(properties.role)
            .build()
        return MapReactiveUserDetailsService(user)
    }
}
