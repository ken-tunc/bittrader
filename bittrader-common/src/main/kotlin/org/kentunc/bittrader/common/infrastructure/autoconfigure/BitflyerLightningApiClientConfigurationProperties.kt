package org.kentunc.bittrader.common.infrastructure.autoconfigure

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "bittrader.external.bitflyer.http")
data class BitflyerLightningApiClientConfigurationProperties(
    val url: String,
    val accessKey: String,
    val secretKey: String,
    val connectTimeout: Int = 1000,
    val readTimeout: Int = 3000
)
