package org.kentunc.bittrader.candle.api.domain.service

import org.kentunc.bittrader.candle.api.domain.model.strategy.IndicatorRuleSet
import org.kentunc.bittrader.candle.api.domain.model.strategy.ema.EmaParams
import org.kentunc.bittrader.candle.api.domain.model.strategy.ema.EmaRuleSet
import org.kentunc.bittrader.candle.api.domain.repository.EmaParamsRepository
import org.springframework.stereotype.Service
import org.ta4j.core.BarSeries

@Service
class EmaStrategyService(emaParamsRepository: EmaParamsRepository) :
    AbstractStrategyService<EmaParams>(emaParamsRepository) {

    override suspend fun buildRuleSet(barSeries: BarSeries, params: EmaParams): IndicatorRuleSet {
        return EmaRuleSet.of(barSeries, params)
    }
}
