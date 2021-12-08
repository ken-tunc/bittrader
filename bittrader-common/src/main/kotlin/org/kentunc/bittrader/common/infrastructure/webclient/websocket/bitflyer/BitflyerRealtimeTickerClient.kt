package org.kentunc.bittrader.common.infrastructure.webclient.websocket.bitflyer

import com.fasterxml.jackson.databind.ObjectMapper
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakeException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.infrastructure.webclient.websocket.bitflyer.model.TickerMessage
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.util.retry.Retry
import java.net.URI

class BitflyerRealtimeTickerClient(
    private val endpoint: URI,
    private val objectMapper: ObjectMapper,
    private val webSocketClient: ReactorNettyWebSocketClient,
    private val retryDurationMillis: Long = 1000L,
) {

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    fun subscribe(produceCodes: Collection<ProductCode>): Flow<TickerMessage> {
        return Flux.create<TickerMessage> { connect(Flux.fromIterable(produceCodes), it) }
            .publish()
            .autoConnect()
            .asFlow()
    }

    private fun connect(productCodes: Flux<ProductCode>, sink: FluxSink<TickerMessage>) {
        webSocketClient.execute(endpoint, BitflyerRealtimeTickerHandler(productCodes, sink, objectMapper))
            .retryWhen(Retry.indefinitely()
                .filter { it is WebSocketClientHandshakeException }
                .doBeforeRetry {
                    log.info("retry subscription...")
                    Thread.sleep(retryDurationMillis)
                })
            .doFinally { sink.complete() }
            .subscribe()
    }
}
