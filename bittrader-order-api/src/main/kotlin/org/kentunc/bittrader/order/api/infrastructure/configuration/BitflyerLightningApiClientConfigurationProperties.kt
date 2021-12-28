package org.kentunc.bittrader.order.api.infrastructure.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "bittrader.external.bitflyer")
data class BitflyerLightningApiClientConfigurationProperties(
    val url: String,
    val accessKey: String,
    val secretKey: String,
    val connectTimeout: Int = 1000,
    val readTimeout: Int = 3000
)
