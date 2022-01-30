package org.kentunc.bittrader.candle.api.presentation.handler

import org.kentunc.bittrader.candle.api.application.StrategyParamsInteractor
import org.kentunc.bittrader.common.presentation.model.strategy.StrategyParamsListRequest
import org.kentunc.bittrader.common.presentation.model.strategy.StrategyParamsSummaryRequest
import org.kentunc.bittrader.common.presentation.model.strategy.StrategyParamsSummaryResponse
import org.kentunc.bittrader.common.presentation.validation.RequestValidator
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait

@Component
class StrategyParamsHandler(
    private val validator: RequestValidator,
    private val strategyParamsInteractor: StrategyParamsInteractor
) {

    suspend fun getParamsSummary(request: ServerRequest): ServerResponse {
        val summaryRequest = request.queryParams()
            .let { StrategyParamsSummaryRequest.from(it) }
        validator.validate(summaryRequest)
        val strategyParamsSummary = strategyParamsInteractor.getStrategyParamsById(summaryRequest.toStrategyValuesId())
            .let { StrategyParamsSummaryResponse.of(it) }
        return ServerResponse.ok()
            .bodyValueAndAwait(strategyParamsSummary)
    }

    suspend fun updateParams(request: ServerRequest): ServerResponse {
        val paramsListRequest = request.awaitBody<StrategyParamsListRequest>()
        validator.validate(paramsListRequest)
        strategyParamsInteractor.saveStrategyParams(
            paramsListRequest.getStrategyValuesId(),
            paramsListRequest.toStrategyParamsList()
        )
        return ServerResponse.noContent().buildAndAwait()
    }
}
