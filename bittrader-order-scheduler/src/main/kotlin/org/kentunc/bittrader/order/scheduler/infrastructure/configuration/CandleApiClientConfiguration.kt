package org.kentunc.bittrader.order.scheduler.infrastructure.configuration

import org.kentunc.bittrader.common.infrastructure.webclient.http.candle.StrategyApiClient
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@EnableConfigurationProperties(CandleApiClientConfigurationProperties::class)
class CandleApiClientConfiguration(private val properties: CandleApiClientConfigurationProperties) {

    @Bean
    fun candleApiWebClient() = WebClient.create(properties.url.toString())

    @Bean
    fun strategyApiClient() = StrategyApiClient(candleApiWebClient())
}
