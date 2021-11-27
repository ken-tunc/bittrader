package org.kentunc.bittrader.common.infrastructure.webclient.candle

import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.kentunc.bittrader.common.presentation.model.ticker.TickerRequest
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitExchange

class CandleApiClient(private val webClient: WebClient) {

    companion object {
        private const val PATH_BASE = "/candles"
    }

    suspend fun feed(tickerRequest: TickerRequest): Void? =
        webClient.put()
            .uri("$PATH_BASE/feed")
            .bodyValue(tickerRequest)
            .awaitExchange { it.releaseBody() }
            .awaitFirstOrNull()
}
