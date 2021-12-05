package org.kentunc.bittrader.common.infrastructure.webclient.http.candle

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.kentunc.bittrader.common.presentation.model.candle.CandleResponse
import org.kentunc.bittrader.common.presentation.model.candle.CandleSearchRequest
import org.kentunc.bittrader.common.presentation.model.ticker.TickerRequest
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlow

class CandleApiClient(private val webClient: WebClient) {

    companion object {
        private const val PATH_BASE = "/candles"
    }

    fun search(candleSearchRequest: CandleSearchRequest): Flow<CandleResponse> =
        webClient.get()
            .uri {
                it.path(PATH_BASE)
                    .queryParams(candleSearchRequest.toMultiValueMap())
                    .build()
            }
            .retrieve()
            .bodyToFlow()

    suspend fun feed(tickerRequest: TickerRequest): Void? =
        webClient.put()
            .uri("$PATH_BASE/feed")
            .bodyValue(tickerRequest)
            .retrieve()
            .toBodilessEntity()
            .then()
            .awaitFirstOrNull()
}
