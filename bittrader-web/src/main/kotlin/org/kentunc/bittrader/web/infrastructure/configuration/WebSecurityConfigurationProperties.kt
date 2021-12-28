package org.kentunc.bittrader.web.infrastructure.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "bittrader.auth")
data class WebSecurityConfigurationProperties(
    val username: String,
    val password: String,
    val role: String,
    val excludePaths: List<String>
)
