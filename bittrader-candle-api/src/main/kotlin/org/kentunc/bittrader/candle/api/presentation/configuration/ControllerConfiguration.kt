package org.kentunc.bittrader.candle.api.presentation.configuration

import org.kentunc.bittrader.common.presentation.configuration.RequestValidatorConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(RequestValidatorConfiguration::class)
class ControllerConfiguration
