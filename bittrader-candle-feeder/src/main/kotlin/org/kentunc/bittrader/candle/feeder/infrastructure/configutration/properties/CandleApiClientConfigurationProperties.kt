package org.kentunc.bittrader.candle.feeder.infrastructure.configutration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.net.URL

@ConstructorBinding
@ConfigurationProperties(prefix = "bittrader.internal.candle-api")
data class CandleApiClientConfigurationProperties(val host: String, val port: Int)
