package org.kentunc.bittrader.candle.api.domain.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.kentunc.bittrader.candle.api.domain.model.toBarSeries
import org.kentunc.bittrader.candle.api.domain.repository.StrategyParamsRepository
import org.kentunc.bittrader.common.domain.model.candle.CandleList
import org.kentunc.bittrader.common.domain.model.strategy.StrategyParams
import org.kentunc.bittrader.common.domain.model.strategy.StrategyValuesId
import org.kentunc.bittrader.common.domain.model.strategy.TradeDecision
import org.kentunc.bittrader.common.domain.model.strategy.TradePosition
import org.springframework.transaction.annotation.Transactional
import org.ta4j.core.BarSeries
import org.ta4j.core.BarSeriesManager
import org.ta4j.core.Strategy
import org.ta4j.core.analysis.criteria.pnl.GrossProfitCriterion

abstract class AbstractStrategyService<T : StrategyParams>(private val strategyParamsRepository: StrategyParamsRepository<T>) :
    StrategyService<T> {

    protected abstract suspend fun buildStrategy(series: BarSeries, params: T): Strategy

    @Transactional(readOnly = true)
    override suspend fun makeOrderDecision(candleList: CandleList, id: StrategyValuesId): TradeDecision<T> {
        val params = strategyParamsRepository.get(id)
        val barSeries = candleList.toBarSeries()
        val strategy = buildStrategy(barSeries, params.values)
        val endIndex = barSeries.endIndex

        if (strategy.shouldEnter(endIndex)) {
            return TradeDecision(params, TradePosition.SHOULD_BUY)
        }
        if (strategy.shouldExit(endIndex)) {
            return TradeDecision(params, TradePosition.SHOULD_SELL)
        }
        return TradeDecision(params, TradePosition.NEUTRAL)
    }

    @Transactional
    override suspend fun optimize(candleList: CandleList, id: StrategyValuesId) {
        val series = candleList.toBarSeries()
        strategyParamsRepository.getForOptimize()
            .map {
                val strategy = buildStrategy(series, it)
                val tradingRecord = BarSeriesManager(series).run(strategy)
                val profit = GrossProfitCriterion().calculate(series, tradingRecord)
                it to profit
            }
            .toList()
            .maxByOrNull { it.second }
            ?.also { strategyParamsRepository.save(id, it.first) }
    }
}
