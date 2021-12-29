package org.kentunc.bittrader.common.infrastructure.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.net.URL

@ConstructorBinding
@ConfigurationProperties(prefix = "bittrader.internal.candle-api")
data class CandleApiClientConfigurationProperties(
    val url: URL,
    val connectTimeout: Int = 1000,
    val readTimeout: Int = 3000
)
