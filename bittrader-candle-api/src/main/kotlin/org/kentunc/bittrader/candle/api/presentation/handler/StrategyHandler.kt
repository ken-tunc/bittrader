package org.kentunc.bittrader.candle.api.presentation.handler

import org.kentunc.bittrader.candle.api.application.StrategyInteractor
import org.kentunc.bittrader.common.presentation.model.strategy.OptimizeRequest
import org.kentunc.bittrader.common.presentation.model.strategy.TradePositionRequest
import org.kentunc.bittrader.common.presentation.model.strategy.TradePositionResponse
import org.kentunc.bittrader.common.presentation.validation.RequestValidator
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait

@Component
class StrategyHandler(private val validator: RequestValidator, private val strategyInteractor: StrategyInteractor) {

    suspend fun getPosition(request: ServerRequest): ServerResponse {
        val positionRequest = request.queryParams()
            .let { TradePositionRequest.from(it) }
        validator.validate(positionRequest)
        val position = strategyInteractor.makeTradingDecision(positionRequest.productCode, positionRequest.duration)
            .totalPosition
        return ServerResponse.ok()
            .bodyValueAndAwait(TradePositionResponse(position))
    }

    suspend fun optimize(request: ServerRequest): ServerResponse {
        val optimizeRequest = request.awaitBody<OptimizeRequest>()
        validator.validate(optimizeRequest)
        strategyInteractor.optimizeStrategies(optimizeRequest.productCode, optimizeRequest.duration)
        return ServerResponse.noContent().buildAndAwait()
    }
}
