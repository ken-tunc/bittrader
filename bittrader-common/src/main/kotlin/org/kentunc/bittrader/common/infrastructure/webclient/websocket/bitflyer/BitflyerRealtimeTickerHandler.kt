package org.kentunc.bittrader.common.infrastructure.webclient.websocket.bitflyer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.infrastructure.webclient.websocket.bitflyer.model.TickerMessage
import org.kentunc.bittrader.common.infrastructure.webclient.websocket.bitflyer.model.TickerRequestParams
import org.kentunc.bittrader.common.infrastructure.webclient.websocket.bitflyer.model.TickerSubscribeParams
import org.kentunc.bittrader.common.infrastructure.webclient.websocket.model.JsonRPC2Request
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

internal class BitflyerRealtimeTickerHandler(
    private val productCodes: Flux<ProductCode>,
    private val sink: FluxSink<TickerMessage>,
    private val objectMapper: ObjectMapper
) : WebSocketHandler {

    companion object {
        private const val SUBSCRIBE_METHOD = "subscribe"
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    override fun handle(session: WebSocketSession): Mono<Void> {
        return productCodes.map { JsonRPC2Request(method = SUBSCRIBE_METHOD, params = TickerRequestParams(it)) }
            .map { session.textMessage(objectMapper.writeValueAsString(it)) }
            .`as`(session::send)
            .thenMany(session.receive())
            .map { it.payloadAsText }
            .map { objectMapper.readValue<JsonRPC2Request<TickerSubscribeParams>>(it).params.message }
            .publishOn(Schedulers.boundedElastic())
            .doOnSubscribe { log.info("subscribe tickers.") }
            .doOnNext { sink.next(it) }
            .doOnTerminate { log.info("ticker subscription terminated.") }
            .then()
    }
}
