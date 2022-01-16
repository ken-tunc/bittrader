package org.kentunc.bittrader.candle.api.application

import org.kentunc.bittrader.candle.api.domain.service.StrategyParamsService
import org.kentunc.bittrader.common.domain.model.strategy.params.StrategyParams
import org.kentunc.bittrader.common.domain.model.strategy.params.StrategyParamsSummary
import org.kentunc.bittrader.common.domain.model.strategy.params.StrategyValuesId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StrategyParamsInteractor(private val strategyParamsService: StrategyParamsService) {

    @Transactional(readOnly = true)
    suspend fun getStrategyParamsById(strategyValuesId: StrategyValuesId): StrategyParamsSummary {
        return strategyParamsService.getSummary(strategyValuesId)
    }

    @Transactional
    suspend fun saveStrategyParams(strategyValuesId: StrategyValuesId, paramsList: List<StrategyParams>) {
        paramsList.forEach { strategyParamsService.save(strategyValuesId, it) }
    }
}
