package org.kentunc.bittrader.candle.api.presentation.handler

import org.kentunc.bittrader.candle.api.application.CandleInteractor
import org.kentunc.bittrader.common.presentation.model.candle.CandleResponse
import org.kentunc.bittrader.common.presentation.model.candle.CandleSearchRequest
import org.kentunc.bittrader.common.presentation.model.ticker.TickerRequest
import org.kentunc.bittrader.common.presentation.validation.RequestValidator
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*

@Component
class CandleHandler(private val validator: RequestValidator, private val candleInteractor: CandleInteractor) {

    suspend fun search(request: ServerRequest): ServerResponse {
        val query = request.queryParams()
            .let { CandleSearchRequest.from(it) }
        validator.validate(query)
        val candles = candleInteractor.findLatestCandles(query.toCandleQuery())
            .toList()
            .map { CandleResponse.of(it) }
        return ServerResponse.ok().bodyValueAndAwait(candles)
    }

    suspend fun feed(request: ServerRequest): ServerResponse {
        val ticker = request.awaitBody<TickerRequest>()
        validator.validate(ticker)
        candleInteractor.feedCandlesByTicker(ticker.toTicker())
        return ServerResponse.noContent().buildAndAwait()
    }
}
