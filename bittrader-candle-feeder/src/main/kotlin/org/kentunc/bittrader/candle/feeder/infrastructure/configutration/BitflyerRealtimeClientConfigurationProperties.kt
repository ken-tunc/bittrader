package org.kentunc.bittrader.candle.feeder.infrastructure.configutration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.net.URI

@ConstructorBinding
@ConfigurationProperties(prefix = "bittrader.external.bitflyer")
data class BitflyerRealtimeClientConfigurationProperties(val endpoint: URI)
