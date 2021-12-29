package org.kentunc.bittrader.common.infrastructure.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.net.URI

@ConstructorBinding
@ConfigurationProperties(prefix = "bittrader.external.bitflyer.websocket")
data class BitflyerRealtimeClientConfigurationProperties(val endpoint: URI)
