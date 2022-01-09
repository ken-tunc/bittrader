package org.kentunc.bittrader.candle.api.application

import org.kentunc.bittrader.candle.api.domain.service.CandleService
import org.kentunc.bittrader.candle.api.domain.service.StrategyService
import org.kentunc.bittrader.common.domain.model.candle.CandleQuery
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.strategy.StrategyValuesId
import org.kentunc.bittrader.common.domain.model.strategy.TradingStrategy
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StrategyInteractor(
    private val candleService: CandleService,
    private val strategyService: StrategyService,
    @Value("\${bittrader.strategy.max-candle-num}") private val maxCandleNum: Int
) {

    @Transactional(readOnly = true)
    suspend fun getTradingStrategy(productCode: ProductCode, duration: Duration): TradingStrategy {
        val candleQuery = CandleQuery(productCode, duration, maxCandleNum)
        val candleList = candleService.findLatest(candleQuery)
        val strategyValuesId = StrategyValuesId(productCode, duration)

        return strategyService.getStrategy(candleList, strategyValuesId)
    }

    @Transactional
    suspend fun optimizeStrategies(productCode: ProductCode, duration: Duration) {
        val candleQuery = CandleQuery(productCode, duration, maxCandleNum)
        val candleList = candleService.findLatest(candleQuery)
        val strategyValuesId = StrategyValuesId(productCode, duration)

        strategyService.optimize(candleList, strategyValuesId)
    }
}
