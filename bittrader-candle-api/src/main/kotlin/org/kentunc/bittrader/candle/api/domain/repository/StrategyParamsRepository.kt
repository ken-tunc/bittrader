package org.kentunc.bittrader.candle.api.domain.repository

import kotlinx.coroutines.flow.Flow
import org.kentunc.bittrader.common.domain.model.strategy.params.StrategyParams
import org.kentunc.bittrader.common.domain.model.strategy.params.StrategyValues
import org.kentunc.bittrader.common.domain.model.strategy.params.StrategyValuesId

interface StrategyParamsRepository<T : StrategyParams> {

    suspend fun get(strategyValuesId: StrategyValuesId): StrategyValues<T>
    fun getForOptimize(): Flow<T>
    suspend fun save(strategyValuesId: StrategyValuesId, params: T): Void?
}
