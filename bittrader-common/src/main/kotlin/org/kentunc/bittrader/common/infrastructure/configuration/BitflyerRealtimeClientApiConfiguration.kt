package org.kentunc.bittrader.common.infrastructure.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import org.kentunc.bittrader.common.infrastructure.webclient.websocket.bitflyer.BitflyerRealtimeTickerClient
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient

@Configuration
@EnableConfigurationProperties(BitflyerRealtimeClientConfigurationProperties::class)
@ConditionalOnProperty(prefix = "bittrader.external.bitflyer.websocket", name = ["endpoint"])
@ConditionalOnBean(ObjectMapper::class)
class BitflyerRealtimeClientApiConfiguration(private val properties: BitflyerRealtimeClientConfigurationProperties) {

    @Bean
    fun bitflyerWebSocketClient() = ReactorNettyWebSocketClient()

    @Bean
    fun bitflyerRealtimeTickerClient(objectMapper: ObjectMapper) =
        BitflyerRealtimeTickerClient(properties.endpoint, objectMapper, bitflyerWebSocketClient())
}
