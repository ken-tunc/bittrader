package org.kentunc.bittrader.candle.api.domain.service

import kotlinx.coroutines.flow.toList
import org.kentunc.bittrader.candle.api.domain.model.strategy.OptimizeParamsSet
import org.kentunc.bittrader.candle.api.domain.model.strategy.StrategyValuesId
import org.kentunc.bittrader.candle.api.domain.model.strategy.TradingStrategy
import org.kentunc.bittrader.candle.api.domain.repository.BBandsParamsRepository
import org.kentunc.bittrader.candle.api.domain.repository.MacdParamsRepository
import org.kentunc.bittrader.common.domain.model.candle.CandleList
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StrategyService(
    private val macdParamsRepository: MacdParamsRepository,
    private val bBandsParamsRepository: BBandsParamsRepository
) {

    @Transactional(readOnly = true)
    suspend fun getStrategy(candleList: CandleList, strategyValuesId: StrategyValuesId): TradingStrategy {
        val macdParams = macdParamsRepository.get(strategyValuesId)
        val bBandsParams = bBandsParamsRepository.get(strategyValuesId)
        return TradingStrategy.of(candleList, macdParams.params, bBandsParams.params)
    }

    @Transactional
    suspend fun optimize(candleList: CandleList, strategyValuesId: StrategyValuesId) {
        val macdParamsForOptimize = macdParamsRepository.getForOptimize().toList()
        val bBandsParamsForOptimize = bBandsParamsRepository.getForOptimize().toList()
        val (bestMacdParams, bestBBandsParams) = OptimizeParamsSet.product(
            macdParamsForOptimize,
            bBandsParamsForOptimize
        )
            .map {
                val profit = TradingStrategy.of(candleList, it.macdParams, it.bBandsParams).getProfit()
                it to profit
            }
            .maxByOrNull { it.second }
            ?.first
            ?: return

        if (bestMacdParams != macdParamsRepository.get(strategyValuesId).params) {
            macdParamsRepository.save(strategyValuesId, bestMacdParams)
        }
        if (bestBBandsParams != bBandsParamsRepository.get(strategyValuesId).params) {
            bBandsParamsRepository.save(strategyValuesId, bestBBandsParams)
        }
    }
}
