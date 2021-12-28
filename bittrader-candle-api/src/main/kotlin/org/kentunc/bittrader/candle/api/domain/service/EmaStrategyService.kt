package org.kentunc.bittrader.candle.api.domain.service

import org.kentunc.bittrader.candle.api.domain.repository.EmaParamsRepository
import org.kentunc.bittrader.common.domain.model.strategy.EmaParams
import org.springframework.stereotype.Service
import org.ta4j.core.BarSeries
import org.ta4j.core.BaseStrategy
import org.ta4j.core.Strategy
import org.ta4j.core.indicators.EMAIndicator
import org.ta4j.core.indicators.helpers.ClosePriceIndicator
import org.ta4j.core.rules.CrossedDownIndicatorRule
import org.ta4j.core.rules.CrossedUpIndicatorRule

@Service
class EmaStrategyService(emaParamsRepository: EmaParamsRepository) :
    AbstractStrategyService<EmaParams>(emaParamsRepository) {

    override suspend fun buildStrategy(series: BarSeries, params: EmaParams): Strategy {
        val closePrice = ClosePriceIndicator(series)
        val shortEma = EMAIndicator(closePrice, params.shortTimeFrame)
        val longEma = EMAIndicator(closePrice, params.longTimeFrame)

        val entryRule = CrossedUpIndicatorRule(shortEma, longEma)
        val exitRule = CrossedDownIndicatorRule(shortEma, longEma)

        return BaseStrategy(entryRule, exitRule)
    }
}
