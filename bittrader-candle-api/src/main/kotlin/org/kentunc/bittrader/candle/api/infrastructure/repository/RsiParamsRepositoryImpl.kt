package org.kentunc.bittrader.candle.api.infrastructure.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.kentunc.bittrader.candle.api.domain.model.strategy.StrategyValues
import org.kentunc.bittrader.candle.api.domain.model.strategy.StrategyValuesId
import org.kentunc.bittrader.candle.api.domain.model.strategy.params.RsiParams
import org.kentunc.bittrader.candle.api.domain.repository.RsiParamsRepository
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.StrategyParamsDao
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.insert
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.selectLatestOne
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.RsiParamsEntity
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.StrategyParamsPrimaryKey

class RsiParamsRepositoryImpl(
    private val defaultParams: RsiParams,
    private val paramsForOptimize: List<RsiParams>,
    private val strategyParamsDao: StrategyParamsDao
) : RsiParamsRepository {

    override suspend fun get(strategyValuesId: StrategyValuesId): StrategyValues<RsiParams> {
        val primaryKey = StrategyParamsPrimaryKey.of(strategyValuesId)
        return strategyParamsDao.selectLatestOne<RsiParamsEntity>(primaryKey)
            ?.toStrategyValues()
            ?: StrategyValues.of(strategyValuesId, defaultParams)
    }

    override fun getForOptimize(): Flow<RsiParams> {
        return paramsForOptimize.asFlow()
    }

    override suspend fun save(strategyValuesId: StrategyValuesId, params: RsiParams): Void? {
        return strategyParamsDao.insert(RsiParamsEntity.of(strategyValuesId, params))
    }
}
