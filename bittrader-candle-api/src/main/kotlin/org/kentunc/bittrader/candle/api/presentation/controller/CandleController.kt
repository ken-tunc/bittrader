package org.kentunc.bittrader.candle.api.presentation.controller

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.kentunc.bittrader.candle.api.application.CandleInteractor
import org.kentunc.bittrader.common.presentation.model.candle.CandleResponse
import org.kentunc.bittrader.common.presentation.model.candle.CandleSearchRequest
import org.kentunc.bittrader.common.presentation.model.ticker.TickerRequest
import org.kentunc.bittrader.common.presentation.validation.RequestValidator
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller

@Controller
@MessageMapping("candle")
class CandleController(private val candleInteractor: CandleInteractor, private val validator: RequestValidator) {

    @MessageMapping("search")
    suspend fun search(request: CandleSearchRequest): Flow<CandleResponse> {
        validator.validate(request)
        return candleInteractor.findLatestCandles(request.toCandleQuery())
            .toList()
            .map { CandleResponse.of(it) }
            .asFlow()
    }

    @MessageMapping("feed")
    suspend fun feed(request: TickerRequest) {
        validator.validate(request)
        candleInteractor.feedCandlesByTicker(request.toTicker())
    }
}
