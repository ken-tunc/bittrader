package org.kentunc.bittrader.candle.api.presentation.handler

import org.kentunc.bittrader.candle.api.application.StrategyInteractor
import org.kentunc.bittrader.common.presentation.model.strategy.StrategyRequest
import org.kentunc.bittrader.common.presentation.model.strategy.TradePositionResponse
import org.kentunc.bittrader.common.presentation.validation.RequestValidator
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait

@Component
class StrategyHandler(private val validator: RequestValidator, private val strategyInteractor: StrategyInteractor) {

    suspend fun getPosition(request: ServerRequest): ServerResponse {
        val strategyRequest = request.queryParams()
            .let { StrategyRequest.from(it) }
        validator.validate(strategyRequest)
        val position = strategyInteractor.makeTradingDecision(strategyRequest.productCode, strategyRequest.duration)
            .totalPosition
        return ServerResponse.ok()
            .bodyValueAndAwait(TradePositionResponse(position))
    }

    suspend fun optimize(request: ServerRequest): ServerResponse {
        val strategyRequest = request.queryParams()
            .let { StrategyRequest.from(it) }
        validator.validate(strategyRequest)
        strategyInteractor.optimizeStrategies(strategyRequest.productCode, strategyRequest.duration)
        return ServerResponse.noContent().buildAndAwait()
    }
}
