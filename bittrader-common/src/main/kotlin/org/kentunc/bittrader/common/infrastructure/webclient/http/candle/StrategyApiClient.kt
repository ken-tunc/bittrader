package org.kentunc.bittrader.common.infrastructure.webclient.http.candle

import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.kentunc.bittrader.common.presentation.model.strategy.OptimizeRequest
import org.kentunc.bittrader.common.presentation.model.strategy.TradePositionRequest
import org.kentunc.bittrader.common.presentation.model.strategy.TradePositionResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

class StrategyApiClient(private val webClient: WebClient) {

    companion object {
        private const val PATH_BASE = "/strategies"
    }

    suspend fun getPosition(request: TradePositionRequest): TradePositionResponse =
        webClient.get()
            .uri {
                it.path("$PATH_BASE/positions")
                    .queryParams(request.toMultiValueMap())
                    .build()
            }
            .retrieve()
            .bodyToMono<TradePositionResponse>()
            .awaitSingle()

    suspend fun optimize(request: OptimizeRequest): Void? =
        webClient.post()
            .uri("$PATH_BASE/optimize")
            .bodyValue(request)
            .retrieve()
            .toBodilessEntity()
            .then()
            .awaitSingleOrNull()
}
