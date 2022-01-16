package org.kentunc.bittrader.candle.api.domain.service

import org.kentunc.bittrader.candle.api.domain.repository.BBandsParamsRepository
import org.kentunc.bittrader.candle.api.domain.repository.EmaParamsRepository
import org.kentunc.bittrader.candle.api.domain.repository.MacdParamsRepository
import org.kentunc.bittrader.candle.api.domain.repository.RsiParamsRepository
import org.kentunc.bittrader.candle.api.domain.repository.StrategyParamsRepository
import org.kentunc.bittrader.common.domain.model.strategy.params.StrategyParams
import org.kentunc.bittrader.common.domain.model.strategy.params.StrategyParamsSummary
import org.kentunc.bittrader.common.domain.model.strategy.params.StrategyValuesId
import org.kentunc.bittrader.common.domain.model.strategy.params.bbands.BBandsParams
import org.kentunc.bittrader.common.domain.model.strategy.params.ema.EmaParams
import org.kentunc.bittrader.common.domain.model.strategy.params.macd.MacdParams
import org.kentunc.bittrader.common.domain.model.strategy.params.rsi.RsiParams
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StrategyParamsService(
    private val bBandsParamsRepository: BBandsParamsRepository,
    private val emaParamsRepository: EmaParamsRepository,
    private val macdParamsRepository: MacdParamsRepository,
    private val rsiParamsRepository: RsiParamsRepository
) {

    @Transactional(readOnly = true)
    suspend fun getSummary(strategyValuesId: StrategyValuesId): StrategyParamsSummary {
        return StrategyParamsSummary(
            id = strategyValuesId,
            bBandsParams = bBandsParamsRepository.get(strategyValuesId).params,
            emaParams = emaParamsRepository.get(strategyValuesId).params,
            macdParams = macdParamsRepository.get(strategyValuesId).params,
            rsiParams = rsiParamsRepository.get(strategyValuesId).params
        )
    }

    @Transactional
    suspend fun <T : StrategyParams> save(strategyValuesId: StrategyValuesId, params: T): Void? {
        return when (params) {
            is BBandsParams -> saveIfUpdated(bBandsParamsRepository, strategyValuesId, params)
            is EmaParams -> saveIfUpdated(emaParamsRepository, strategyValuesId, params)
            is MacdParams -> saveIfUpdated(macdParamsRepository, strategyValuesId, params)
            is RsiParams -> saveIfUpdated(rsiParamsRepository, strategyValuesId, params)
            else -> throw IllegalArgumentException("Illegal strategy params type: ${params::class.simpleName}")
        }
    }

    private suspend fun <T : StrategyParams> saveIfUpdated(
        paramsRepository: StrategyParamsRepository<T>,
        strategyValuesId: StrategyValuesId,
        params: T
    ): Void? {
        val currentParams = paramsRepository.get(strategyValuesId).params
        if (currentParams == params) {
            return null
        }
        return paramsRepository.save(strategyValuesId, params)
    }
}
