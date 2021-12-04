package org.kentunc.bittrader.common.infrastructure.webclient.websocket.bitflyer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.infrastructure.webclient.websocket.bitflyer.model.TickerMessage
import org.kentunc.bittrader.common.infrastructure.webclient.websocket.bitflyer.model.TickerRequestParams
import org.kentunc.bittrader.common.infrastructure.webclient.websocket.bitflyer.model.TickerSubscribeParams
import org.kentunc.bittrader.common.infrastructure.webclient.websocket.model.JsonRPC2Request
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
import reactor.core.scheduler.Schedulers
import java.net.URI

class BitflyerRealtimeTickerClient(
    private val endpoint: URI,
    private val objectMapper: ObjectMapper,
    private val webSocketClient: ReactorNettyWebSocketClient,
    private val terminateCallback: (productCode: ProductCode) -> Unit = { log.info("$it subscription terminated.") }
) {

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
        private const val SUBSCRIBE_METHOD = "subscribe"
    }

    fun subscribe(productCode: ProductCode): Flow<TickerMessage> {
        val buffer = Sinks.many().multicast().onBackpressureBuffer<TickerMessage>()

        val sessionMono = webSocketClient.execute(endpoint) { session ->
            val request = JsonRPC2Request(method = SUBSCRIBE_METHOD, params = TickerRequestParams(productCode))
            val requestMessage = Mono.fromCallable {
                session.textMessage(objectMapper.writeValueAsString(request))
            }

            session.send(requestMessage)
                .thenMany(
                    session.receive()
                        .map(WebSocketMessage::getPayloadAsText)
                        .map { objectMapper.readValue<JsonRPC2Request<TickerSubscribeParams>>(it).params.message }
                        .doOnNext { buffer.tryEmitNext(it) }
                        .doOnTerminate { terminateCallback(productCode) }
                        .then())
                .then()
        }

        return buffer.asFlux()
            .publishOn(Schedulers.boundedElastic())
            .doOnSubscribe { sessionMono.subscribe() }
            .asFlow()
    }
}
