package org.kentunc.bittrader.candle.api.infrastructure.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.kentunc.bittrader.candle.api.domain.repository.EmaParamsRepository
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.StrategyParamsDao
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.insert
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.selectLatestOne
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.EmaParamsEntity
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.StrategyParamsPrimaryKey
import org.kentunc.bittrader.common.domain.model.strategy.EmaParams
import org.kentunc.bittrader.common.domain.model.strategy.StrategyValues
import org.kentunc.bittrader.common.domain.model.strategy.StrategyValuesId

class EmaParamsRepositoryImpl(
    private val defaultParams: EmaParams,
    private val paramsForOptimize: List<EmaParams>,
    private val strategyParamsDao: StrategyParamsDao
) : EmaParamsRepository {

    override suspend fun get(strategyValuesId: StrategyValuesId): StrategyValues<EmaParams> {
        val primaryKey = StrategyParamsPrimaryKey.of(strategyValuesId)
        return strategyParamsDao.selectLatestOne<EmaParamsEntity>(primaryKey)
            ?.toStrategyValues()
            ?: StrategyValues.of(strategyValuesId, defaultParams)
    }

    override fun getForOptimize(): Flow<EmaParams> {
        return paramsForOptimize.asFlow()
    }

    override suspend fun save(strategyValuesId: StrategyValuesId, params: EmaParams): Void? {
        return strategyParamsDao.insert(EmaParamsEntity.of(strategyValuesId, params))
    }
}
