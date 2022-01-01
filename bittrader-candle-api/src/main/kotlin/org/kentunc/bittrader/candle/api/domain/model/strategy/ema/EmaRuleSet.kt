package org.kentunc.bittrader.candle.api.domain.model.strategy.ema

import org.kentunc.bittrader.candle.api.domain.model.strategy.IndicatorRuleSet
import org.ta4j.core.BarSeries
import org.ta4j.core.Rule
import org.ta4j.core.indicators.EMAIndicator
import org.ta4j.core.indicators.helpers.ClosePriceIndicator
import org.ta4j.core.rules.CrossedDownIndicatorRule
import org.ta4j.core.rules.CrossedUpIndicatorRule

class EmaRuleSet private constructor(barSeries: BarSeries, params: EmaParams) : IndicatorRuleSet {

    override val entryRule: Rule
    override val exitRule: Rule

    init {
        val closePrice = ClosePriceIndicator(barSeries)
        val shortEma = EMAIndicator(closePrice, params.shortTimeFrame)
        val longEma = EMAIndicator(closePrice, params.longTimeFrame)

        entryRule = CrossedUpIndicatorRule(shortEma, longEma)
        exitRule = CrossedDownIndicatorRule(shortEma, longEma)
    }

    companion object {
        fun of(barSeries: BarSeries, params: EmaParams) = EmaRuleSet(barSeries, params)
    }
}
