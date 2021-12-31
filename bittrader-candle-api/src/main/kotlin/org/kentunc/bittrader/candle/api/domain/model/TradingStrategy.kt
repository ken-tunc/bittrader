package org.kentunc.bittrader.candle.api.domain.model

import org.kentunc.bittrader.common.domain.model.strategy.TradingPosition
import org.ta4j.core.BarSeries
import org.ta4j.core.BaseStrategy
import org.ta4j.core.Strategy

class TradingStrategy private constructor(ruleSets: List<IndicatorRuleSet>, barSeries: BarSeries) {

    private val strategy: Strategy
    private val endIndex: Int

    init {
        val entryRule = ruleSets.map { it.entryRule }
            .reduce { acc, rule -> acc.and(rule) }
        val exitRule = ruleSets.map { it.exitRule }
            .reduce { acc, rule -> acc.or(rule) }
        strategy = BaseStrategy(entryRule, exitRule)
        endIndex = barSeries.endIndex
    }

    val position: TradingPosition
        get() {
            if (strategy.shouldEnter(endIndex)) {
                return TradingPosition.SHOULD_BUY
            }
            if (strategy.shouldExit(endIndex)) {
                return TradingPosition.SHOULD_SELL
            }
            return TradingPosition.NEUTRAL
        }

    companion object {
        fun of(ruleSets: List<IndicatorRuleSet>, barSeries: BarSeries) = TradingStrategy(ruleSets, barSeries)
    }
}
