package org.kentunc.bittrader.candle.api.presentation.handler

import org.kentunc.bittrader.candle.api.application.CandleInteractor
import org.kentunc.bittrader.common.presentation.model.ticker.TickerRequest
import org.kentunc.bittrader.common.presentation.validation.RequestValidator
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.buildAndAwait

@Component
class CandleHandler(private val validator: RequestValidator, private val candleInteractor: CandleInteractor) {

    suspend fun feed(request: ServerRequest): ServerResponse {
        val ticker = request.awaitBody<TickerRequest>()
        validator.validate(ticker)
        candleInteractor.feedCandlesByTicker(ticker.toModel())
        return ServerResponse.noContent().buildAndAwait()
    }
}
