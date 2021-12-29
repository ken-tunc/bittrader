package org.kentunc.bittrader.common.infrastructure.configuration

import org.kentunc.bittrader.common.infrastructure.webclient.http.connector.ClientHttpConnectorFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ClientHttpConnectorFactoryAutoConfiguration {

    @Bean
    fun clientHttpConnectorFactory() = ClientHttpConnectorFactory()
}
