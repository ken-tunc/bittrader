package org.kentunc.bittrader.candle.api.domain.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.kentunc.bittrader.candle.api.domain.model.strategy.IndicatorRuleSet
import org.kentunc.bittrader.candle.api.domain.model.strategy.StrategyParams
import org.kentunc.bittrader.candle.api.domain.model.strategy.StrategyValuesId
import org.kentunc.bittrader.candle.api.domain.repository.StrategyParamsRepository
import org.springframework.transaction.annotation.Transactional
import org.ta4j.core.BarSeries
import org.ta4j.core.BarSeriesManager
import org.ta4j.core.analysis.criteria.pnl.GrossProfitCriterion

abstract class AbstractStrategyService<T : StrategyParams>(private val strategyParamsRepository: StrategyParamsRepository<T>) :
    StrategyService<T> {

    abstract suspend fun buildRuleSet(barSeries: BarSeries, params: T): IndicatorRuleSet

    @Transactional(readOnly = true)
    override suspend fun getRuleSet(barSeries: BarSeries, id: StrategyValuesId): IndicatorRuleSet {
        val strategyValues = strategyParamsRepository.get(id)
        return buildRuleSet(barSeries, strategyValues.params)
    }

    @Transactional
    override suspend fun optimize(barSeries: BarSeries, id: StrategyValuesId) {
        val optimizedParams = strategyParamsRepository.getForOptimize()
            .map {
                val strategy = getRuleSet(barSeries, id).buildStrategy()
                val tradingRecord = BarSeriesManager(barSeries).run(strategy)
                val profit = GrossProfitCriterion().calculate(barSeries, tradingRecord)
                it to profit
            }
            .toList()
            .maxByOrNull { it.second }
            ?.first
            ?: return

        val currentParams = strategyParamsRepository.get(id)
        if (currentParams.params != optimizedParams) {
            strategyParamsRepository.save(id, optimizedParams)
        }
    }
}
