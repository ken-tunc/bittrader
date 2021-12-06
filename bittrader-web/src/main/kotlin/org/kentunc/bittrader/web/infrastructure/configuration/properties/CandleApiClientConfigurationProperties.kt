package org.kentunc.bittrader.web.infrastructure.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.net.URL

@ConstructorBinding
@ConfigurationProperties(prefix = "bittrader.internal.candle-api")
data class CandleApiClientConfigurationProperties(val url: URL)
