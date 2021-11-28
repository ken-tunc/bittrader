package org.kentunc.bittrader.candle.feeder.infrastructure.configutration.webclient

import com.fasterxml.jackson.databind.ObjectMapper
import org.kentunc.bittrader.candle.feeder.infrastructure.configutration.properties.BitflyerRealtimeClientConfigurationProperties
import org.kentunc.bittrader.common.infrastructure.webclient.websocket.bitflyer.BitflyerRealtimeTickerClient
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient

@Configuration
@EnableConfigurationProperties(BitflyerRealtimeClientConfigurationProperties::class)
class BitflyerRealtimeClientConfiguration(private val properties: BitflyerRealtimeClientConfigurationProperties) {

    @Bean
    fun webSocketClient() = ReactorNettyWebSocketClient()

    @Bean
    fun bitflyerRealtimeTickerClient(objectMapper: ObjectMapper) =
        BitflyerRealtimeTickerClient(properties.endpoint, objectMapper, webSocketClient())
}
