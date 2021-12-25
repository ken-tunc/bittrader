package org.kentunc.bittrader.web.infrastructure.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.net.URL

@ConstructorBinding
@ConfigurationProperties(prefix = "bittrader.internal.order-api")
data class OrderApiClientConfigurationProperties(val url: URL)