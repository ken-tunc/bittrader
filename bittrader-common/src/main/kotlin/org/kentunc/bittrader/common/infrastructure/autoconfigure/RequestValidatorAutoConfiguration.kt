package org.kentunc.bittrader.common.infrastructure.autoconfigure

import org.kentunc.bittrader.common.presentation.validation.RequestValidator
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.validation.Validator

@Configuration
@ConditionalOnWebApplication
@ConditionalOnBean(Validator::class)
class RequestValidatorAutoConfiguration {

    @Bean
    fun requestValidator(validator: Validator) = RequestValidator(validator)
}
