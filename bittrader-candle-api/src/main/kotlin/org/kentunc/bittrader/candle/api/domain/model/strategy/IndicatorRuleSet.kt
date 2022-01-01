package org.kentunc.bittrader.candle.api.domain.model.strategy

import org.ta4j.core.BaseStrategy
import org.ta4j.core.Rule
import org.ta4j.core.Strategy

interface IndicatorRuleSet {

    val entryRule: Rule
    val exitRule: Rule

    fun buildStrategy(): Strategy = BaseStrategy(entryRule, exitRule)
}
