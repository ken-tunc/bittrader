package org.kentunc.bittrader.common.infrastructure.webclient.rsocket.candle

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.kentunc.bittrader.common.presentation.model.candle.CandleResponse
import org.kentunc.bittrader.common.presentation.model.candle.CandleSearchRequest
import org.kentunc.bittrader.common.presentation.model.ticker.TickerRequest
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.retrieveFlux

class CandleApiClient(private val requester: RSocketRequester) {

    fun search(candleSearchRequest: CandleSearchRequest): Flow<CandleResponse> =
        requester.route("candle.search")
            .data(candleSearchRequest)
            .retrieveFlux<CandleResponse>()
            .asFlow()

    suspend fun feed(tickerRequest: TickerRequest): Void? =
        requester.route("candle.feed")
            .data(tickerRequest)
            .send()
            .awaitFirstOrNull()

}
