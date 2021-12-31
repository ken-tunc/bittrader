package org.kentunc.bittrader.candle.api.domain.service

import org.kentunc.bittrader.candle.api.domain.model.TradingStrategy
import org.kentunc.bittrader.candle.api.domain.model.toBarSeries
import org.kentunc.bittrader.common.domain.model.candle.CandleList
import org.kentunc.bittrader.common.domain.model.strategy.StrategyValuesId
import org.springframework.stereotype.Service

@Service
class CompositeStrategyService(emaStrategyService: EmaStrategyService) {

    private val strategyServices = listOf(emaStrategyService)

    suspend fun getStrategy(candleList: CandleList, strategyValuesId: StrategyValuesId): TradingStrategy {
        val barSeries = candleList.toBarSeries()
        val ruleSets = strategyServices.map { it.getRuleSet(barSeries, strategyValuesId) }
        return TradingStrategy.of(ruleSets, barSeries)
    }

    suspend fun optimize(candleList: CandleList, strategyValuesId: StrategyValuesId) {
        val barSeries = candleList.toBarSeries()
        strategyServices.forEach { it.optimize(barSeries, strategyValuesId) }
    }
}
