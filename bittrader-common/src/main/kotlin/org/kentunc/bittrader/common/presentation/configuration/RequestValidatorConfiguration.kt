package org.kentunc.bittrader.common.presentation.configuration

import org.kentunc.bittrader.common.presentation.validation.RequestValidator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.validation.Validator

@Configuration
class RequestValidatorConfiguration {

    @Bean
    fun requestValidator(validator: Validator) = RequestValidator(validator)
}
