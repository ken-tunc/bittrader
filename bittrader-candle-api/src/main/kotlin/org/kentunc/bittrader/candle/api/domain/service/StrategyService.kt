package org.kentunc.bittrader.candle.api.domain.service

import kotlinx.coroutines.flow.toList
import org.kentunc.bittrader.candle.api.domain.model.strategy.OptimizeParamsSet
import org.kentunc.bittrader.candle.api.domain.model.strategy.StrategyValuesId
import org.kentunc.bittrader.candle.api.domain.model.strategy.TradingStrategy
import org.kentunc.bittrader.candle.api.domain.repository.BBandsParamsRepository
import org.kentunc.bittrader.candle.api.domain.repository.RsiParamsRepository
import org.kentunc.bittrader.common.domain.model.candle.CandleList
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StrategyService(
    private val rsiParamsRepository: RsiParamsRepository,
    private val bBandsParamsRepository: BBandsParamsRepository
) {

    @Transactional(readOnly = true)
    suspend fun getStrategy(candleList: CandleList, strategyValuesId: StrategyValuesId): TradingStrategy {
        val rsiParams = rsiParamsRepository.get(strategyValuesId)
        val bBandsParams = bBandsParamsRepository.get(strategyValuesId)
        return TradingStrategy.of(candleList, rsiParams.params, bBandsParams.params)
    }

    @Transactional(readOnly = true)
    suspend fun optimize(candleList: CandleList, strategyValuesId: StrategyValuesId): OptimizeParamsSet? {
        val rsiParamsForOptimize = rsiParamsRepository.getForOptimize().toList()
        val bBandsParamsForOptimize = bBandsParamsRepository.getForOptimize().toList()
        return OptimizeParamsSet.product(rsiParamsForOptimize, bBandsParamsForOptimize)
            .map {
                val profit = TradingStrategy.of(candleList, it.rsiParams, it.bBandsParams).getCriterionValue()
                it to profit
            }
            .maxByOrNull { it.second }
            ?.first
    }

    @Transactional
    suspend fun updateParams(strategyValuesId: StrategyValuesId, params: OptimizeParamsSet) {
        if (params.rsiParams != rsiParamsRepository.get(strategyValuesId).params) {
            rsiParamsRepository.save(strategyValuesId, params.rsiParams)
        }
        if (params.bBandsParams != bBandsParamsRepository.get(strategyValuesId).params) {
            bBandsParamsRepository.save(strategyValuesId, params.bBandsParams)
        }
    }
}
