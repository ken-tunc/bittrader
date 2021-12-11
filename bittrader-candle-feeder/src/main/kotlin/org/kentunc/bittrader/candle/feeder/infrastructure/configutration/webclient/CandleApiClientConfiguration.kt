package org.kentunc.bittrader.candle.feeder.infrastructure.configutration.webclient

import org.kentunc.bittrader.candle.feeder.infrastructure.configutration.properties.CandleApiClientConfigurationProperties
import org.kentunc.bittrader.common.infrastructure.webclient.rsocket.candle.CandleApiClient
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.util.MimeTypeUtils

@Configuration
@EnableConfigurationProperties(CandleApiClientConfigurationProperties::class)
class CandleApiClientConfiguration(private val properties: CandleApiClientConfigurationProperties) {

    @Bean
    fun rSocketRequester(builder: RSocketRequester.Builder) =
        builder.dataMimeType(MimeTypeUtils.APPLICATION_JSON)
            .tcp(properties.host, properties.port)

    @Bean
    fun candleApiClient(rSocketRequester: RSocketRequester) = CandleApiClient(rSocketRequester)
}
