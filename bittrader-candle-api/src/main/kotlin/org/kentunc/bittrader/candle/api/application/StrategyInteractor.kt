package org.kentunc.bittrader.candle.api.application

import org.kentunc.bittrader.candle.api.application.model.TotalOrderDecision
import org.kentunc.bittrader.candle.api.domain.service.CandleService
import org.kentunc.bittrader.candle.api.domain.service.EmaStrategyService
import org.kentunc.bittrader.candle.api.domain.service.StrategyService
import org.kentunc.bittrader.common.domain.model.candle.CandleQuery
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.strategy.StrategyValuesId
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.common.shared.extension.forEachParallel
import org.kentunc.bittrader.common.shared.extension.mapParallel
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StrategyInteractor(
    private val candleService: CandleService,
    emaStrategyService: EmaStrategyService,
    @Value("\${bittrader.strategy.max-candle-num}") private val maxCandleNum: Int
) {

    private val strategyServices: List<StrategyService<*>> = listOf(emaStrategyService)

    @Transactional(readOnly = true)
    suspend fun makeTradingDecision(productCode: ProductCode, duration: Duration): TotalOrderDecision {
        val candleQuery = CandleQuery(productCode, duration, maxCandleNum)
        val candleList = candleService.findLatest(candleQuery)
        val strategyValuesId = StrategyValuesId(productCode, duration)

        val decisions = strategyServices.mapParallel { it.makeOrderDecision(candleList, strategyValuesId) }
        return TotalOrderDecision.of(decisions)
    }

    @Transactional
    suspend fun optimizeStrategies(productCode: ProductCode, duration: Duration) {
        val candleQuery = CandleQuery(productCode, duration, maxCandleNum)
        val candleList = candleService.findLatest(candleQuery)
        val strategyValuesId = StrategyValuesId(productCode, duration)

        strategyServices.forEachParallel {
            it.optimize(candleList, strategyValuesId)
        }
    }
}
