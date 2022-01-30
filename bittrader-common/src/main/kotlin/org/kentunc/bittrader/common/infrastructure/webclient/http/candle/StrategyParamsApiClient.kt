package org.kentunc.bittrader.common.infrastructure.webclient.http.candle

import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.kentunc.bittrader.common.presentation.model.strategy.StrategyParamsListRequest
import org.kentunc.bittrader.common.presentation.model.strategy.StrategyParamsSummaryRequest
import org.kentunc.bittrader.common.presentation.model.strategy.StrategyParamsSummaryResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

class StrategyParamsApiClient(private val webClient: WebClient) {

    companion object {
        private const val PATH_BASE = "/strategies/params"
    }

    suspend fun getSummary(request: StrategyParamsSummaryRequest): StrategyParamsSummaryResponse =
        webClient.get()
            .uri {
                it.path("/summary")
                    .queryParams(request.toMultiValueMap())
                    .build()
            }
            .retrieve()
            .bodyToMono<StrategyParamsSummaryResponse>()
            .awaitSingle()

    suspend fun optimize(request: StrategyParamsListRequest): Void? =
        webClient.post()
            .uri(PATH_BASE)
            .bodyValue(request)
            .retrieve()
            .toBodilessEntity()
            .then()
            .awaitSingleOrNull()
}
