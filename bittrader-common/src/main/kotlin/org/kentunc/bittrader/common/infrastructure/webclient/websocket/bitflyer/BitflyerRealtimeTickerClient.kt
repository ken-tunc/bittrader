package org.kentunc.bittrader.common.infrastructure.webclient.websocket.bitflyer

import com.fasterxml.jackson.databind.ObjectMapper
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakeException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.infrastructure.webclient.websocket.bitflyer.model.TickerMessage
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.util.retry.Retry
import java.net.URI
import java.time.Duration

class BitflyerRealtimeTickerClient(
    private val endpoint: URI,
    private val objectMapper: ObjectMapper,
    private val webSocketClient: ReactorNettyWebSocketClient,
    private val retry: Retry = DEFAULT_RETRY
) {

    companion object {
        private val RETRYABLE_STATUS_CODES = setOf(
            HttpResponseStatus.INTERNAL_SERVER_ERROR,
            HttpResponseStatus.BAD_GATEWAY,
            HttpResponseStatus.SERVICE_UNAVAILABLE,
            HttpResponseStatus.GATEWAY_TIMEOUT
        )
        private val DEFAULT_RETRY = Retry.fixedDelay(Long.MAX_VALUE, Duration.ofMillis(1000))
            .filter {
                it is WebSocketClientHandshakeException && RETRYABLE_STATUS_CODES.contains(it.response().status())
            }
    }

    fun subscribe(produceCodes: Collection<ProductCode>): Flow<TickerMessage> {
        return Flux.create<TickerMessage> { connect(Flux.fromIterable(produceCodes), it) }
            .publish()
            .autoConnect()
            .asFlow()
    }

    private fun connect(productCodes: Flux<ProductCode>, sink: FluxSink<TickerMessage>) {
        webSocketClient.execute(endpoint, BitflyerRealtimeTickerHandler(productCodes, sink, objectMapper))
            .retryWhen(retry)
            .doFinally { sink.complete() }
            .subscribe()
    }
}
