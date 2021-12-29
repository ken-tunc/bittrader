package org.kentunc.bittrader.common.infrastructure.configuration

import org.kentunc.bittrader.common.infrastructure.webclient.http.candle.CandleApiClient
import org.kentunc.bittrader.common.infrastructure.webclient.http.candle.StrategyApiClient
import org.kentunc.bittrader.common.infrastructure.webclient.http.connector.ClientHttpConnectorFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@EnableConfigurationProperties(CandleApiClientConfigurationProperties::class)
@ConditionalOnProperty(prefix = "bittrader.internal.candle-api", name = ["url"])
class CandleApiClientAutoConfiguration(
    private val properties: CandleApiClientConfigurationProperties,
    private val connectorFactory: ClientHttpConnectorFactory
) {
    @Bean
    fun candleApiWebClient(): WebClient {
        val connector = connectorFactory.create(properties.connectTimeout, properties.readTimeout)
        return WebClient.builder()
            .baseUrl(properties.url.toString())
            .clientConnector(connector)
            .build()
    }

    @Bean
    fun candleApiClient() = CandleApiClient(candleApiWebClient())

    @Bean
    fun strategyApiClient() = StrategyApiClient(candleApiWebClient())
}
