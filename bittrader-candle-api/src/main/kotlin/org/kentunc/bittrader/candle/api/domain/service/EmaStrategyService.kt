package org.kentunc.bittrader.candle.api.domain.service

import org.kentunc.bittrader.candle.api.domain.model.EmaRuleSet
import org.kentunc.bittrader.candle.api.domain.model.IndicatorRuleSet
import org.kentunc.bittrader.candle.api.domain.repository.EmaParamsRepository
import org.kentunc.bittrader.common.domain.model.strategy.EmaParams
import org.springframework.stereotype.Service
import org.ta4j.core.BarSeries

@Service
class EmaStrategyService(emaParamsRepository: EmaParamsRepository) :
    AbstractStrategyService<EmaParams>(emaParamsRepository) {

    override suspend fun buildRuleSet(barSeries: BarSeries, params: EmaParams): IndicatorRuleSet {
        return EmaRuleSet(barSeries, params)
    }
}
