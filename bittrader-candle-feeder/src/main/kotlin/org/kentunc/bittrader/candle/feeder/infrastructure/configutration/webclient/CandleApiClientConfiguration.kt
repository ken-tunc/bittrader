package org.kentunc.bittrader.candle.feeder.infrastructure.configutration.webclient

import org.kentunc.bittrader.candle.feeder.infrastructure.configutration.properties.CandleApiClientConfigurationProperties
import org.kentunc.bittrader.common.infrastructure.webclient.http.candle.CandleApiClient
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
    fun candleApiClient() = CandleApiClient(candleApiWebClient())
}
