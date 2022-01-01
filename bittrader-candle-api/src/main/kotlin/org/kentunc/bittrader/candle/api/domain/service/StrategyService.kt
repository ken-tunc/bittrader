package org.kentunc.bittrader.candle.api.domain.service

import kotlinx.coroutines.flow.toList
import org.kentunc.bittrader.candle.api.domain.model.strategy.OptimizeParamsSet
import org.kentunc.bittrader.candle.api.domain.model.strategy.StrategyValuesId
import org.kentunc.bittrader.candle.api.domain.model.strategy.TradingStrategy
import org.kentunc.bittrader.candle.api.domain.repository.MacdParamsRepository
import org.kentunc.bittrader.candle.api.domain.repository.RsiParamsRepository
import org.kentunc.bittrader.common.domain.model.candle.CandleList
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StrategyService(
    private val macdParamsRepository: MacdParamsRepository,
    private val rsiParamsRepository: RsiParamsRepository
) {

    @Transactional(readOnly = true)
    suspend fun getStrategy(candleList: CandleList, strategyValuesId: StrategyValuesId): TradingStrategy {
        val macdParams = macdParamsRepository.get(strategyValuesId)
        val rsiParams = rsiParamsRepository.get(strategyValuesId)
        return TradingStrategy.of(candleList, macdParams.params, rsiParams.params)
    }

    @Transactional
    suspend fun optimize(candleList: CandleList, strategyValuesId: StrategyValuesId) {
        val macdParamsForOptimize = macdParamsRepository.getForOptimize().toList()
        val rsiParamsForOptimize = rsiParamsRepository.getForOptimize().toList()
        val (bestMacdParams, bestRsiParams) = OptimizeParamsSet.product(macdParamsForOptimize, rsiParamsForOptimize)
            .map {
                val profit = TradingStrategy.of(candleList, it.macdParams, it.rsiParams).getProfit()
                it to profit
            }
            .maxByOrNull { it.second }
            ?.first
            ?: return

        if (bestMacdParams != macdParamsRepository.get(strategyValuesId).params) {
            macdParamsRepository.save(strategyValuesId, bestMacdParams)
        }
        if (bestRsiParams != rsiParamsRepository.get(strategyValuesId).params) {
            rsiParamsRepository.save(strategyValuesId, bestRsiParams)
        }
    }
}
