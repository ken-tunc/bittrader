package org.kentunc.bittrader.candle.api.application

import org.kentunc.bittrader.candle.api.domain.model.strategy.StrategyValuesId
import org.kentunc.bittrader.candle.api.domain.model.strategy.TradingStrategy
import org.kentunc.bittrader.candle.api.domain.service.CandleService
import org.kentunc.bittrader.candle.api.domain.service.StrategyService
import org.kentunc.bittrader.common.domain.model.candle.CandleQuery
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class StrategyInteractor(
    private val candleService: CandleService,
    private val strategyService: StrategyService,
    @Value("\${bittrader.strategy.strategy-candle-num}") private val strategyCandleNum: Int,
    @Value("\${bittrader.strategy.optimize-candle-num}") private val optimizeCandleNum: Int,
) {

    suspend fun getTradingStrategy(productCode: ProductCode, duration: Duration): TradingStrategy {
        val candleQuery = CandleQuery(productCode, duration, strategyCandleNum)
        val candleList = candleService.findLatest(candleQuery)
        val strategyValuesId = StrategyValuesId(productCode, duration)

        return strategyService.getStrategy(candleList, strategyValuesId)
    }

    suspend fun optimizeStrategies(productCode: ProductCode, duration: Duration) {
        val candleQuery = CandleQuery(productCode, duration, optimizeCandleNum)
        val candleList = candleService.findLatest(candleQuery)
        val strategyValuesId = StrategyValuesId(productCode, duration)

        strategyService.optimize(candleList, strategyValuesId)
            ?.also { strategyService.updateParams(strategyValuesId, it) }
    }
}
