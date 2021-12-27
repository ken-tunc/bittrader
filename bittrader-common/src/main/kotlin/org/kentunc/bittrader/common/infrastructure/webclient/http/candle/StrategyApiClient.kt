package org.kentunc.bittrader.common.infrastructure.webclient.http.candle

import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.kentunc.bittrader.common.presentation.model.strategy.StrategyRequest
import org.kentunc.bittrader.common.presentation.model.strategy.TradePositionResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

class StrategyApiClient(private val webClient: WebClient) {

    companion object {
        private const val PATH_BASE = "/strategies"
    }

    suspend fun getPosition(request: StrategyRequest): TradePositionResponse =
        webClient.get()
            .uri {
                it.path("$PATH_BASE/positions")
                    .queryParams(request.toMultiValueMap())
                    .build()
            }
            .retrieve()
            .bodyToMono<TradePositionResponse>()
            .awaitSingle()

    suspend fun optimize(request: StrategyRequest): Void? =
        webClient.post()
            .uri {
                it.path("$PATH_BASE/positions")
                    .queryParams(request.toMultiValueMap())
                    .build()
            }
            .retrieve()
            .toBodilessEntity()
            .then()
            .awaitSingleOrNull()
}
