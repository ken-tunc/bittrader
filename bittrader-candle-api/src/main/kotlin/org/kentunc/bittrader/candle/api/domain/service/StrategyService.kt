package org.kentunc.bittrader.candle.api.domain.service

import org.kentunc.bittrader.candle.api.domain.model.IndicatorRuleSet
import org.kentunc.bittrader.common.domain.model.strategy.StrategyParams
import org.kentunc.bittrader.common.domain.model.strategy.StrategyValuesId
import org.ta4j.core.BarSeries

interface StrategyService<T : StrategyParams> {

    suspend fun getRuleSet(barSeries: BarSeries, id: StrategyValuesId): IndicatorRuleSet

    suspend fun optimize(barSeries: BarSeries, id: StrategyValuesId)
}
