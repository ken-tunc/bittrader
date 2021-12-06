package org.kentunc.bittrader.web.infrastructure.configuration

import org.kentunc.bittrader.web.infrastructure.configuration.properties.WebjarsConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WebjarsConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "bittrader.webjars")
    fun webjarsConfig() = WebjarsConfigurationProperties()
}
