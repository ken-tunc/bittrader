package org.kentunc.bittrader.common.infrastructure.webclient.websocket.bitflyer

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import java.net.URI

internal class BitflyerRealtimeTickerClientTest {

    private lateinit var target: BitflyerRealtimeTickerClient

    @BeforeEach
    internal fun setUp() {
        target = BitflyerRealtimeTickerClient(
            endpoint = URI.create("wss://ws.lightstream.bitflyer.com/json-rpc"),
            objectMapper = jacksonObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false),
            webSocketClient = ReactorNettyWebSocketClient()
        )
    }

    @ParameterizedTest
    @EnumSource(ProductCode::class)
    fun `subscribe three tickers`(productCode: ProductCode) = runBlocking {
        target.subscribe(productCode)
            .take(3)
            .collect {
                assertEquals(productCode, it.productCode)
            }
    }
}
