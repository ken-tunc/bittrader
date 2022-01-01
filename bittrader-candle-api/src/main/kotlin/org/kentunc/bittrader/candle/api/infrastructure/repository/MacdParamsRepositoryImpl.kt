package org.kentunc.bittrader.candle.api.infrastructure.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.kentunc.bittrader.candle.api.domain.model.strategy.StrategyValues
import org.kentunc.bittrader.candle.api.domain.model.strategy.StrategyValuesId
import org.kentunc.bittrader.candle.api.domain.model.strategy.params.MacdParams
import org.kentunc.bittrader.candle.api.domain.repository.MacdParamsRepository
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.StrategyParamsDao
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.insert
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.selectLatestOne
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.MacdParamsEntity
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.StrategyParamsPrimaryKey

class MacdParamsRepositoryImpl(
    private val defaultParams: MacdParams,
    private val paramsForOptimize: List<MacdParams>,
    private val strategyParamsDao: StrategyParamsDao
) : MacdParamsRepository {

    override suspend fun get(strategyValuesId: StrategyValuesId): StrategyValues<MacdParams> {
        val primaryKey = StrategyParamsPrimaryKey.of(strategyValuesId)
        return strategyParamsDao.selectLatestOne<MacdParamsEntity>(primaryKey)
            ?.toStrategyValues()
            ?: StrategyValues.of(strategyValuesId, defaultParams)
    }

    override fun getForOptimize(): Flow<MacdParams> {
        return paramsForOptimize.asFlow()
    }

    override suspend fun save(strategyValuesId: StrategyValuesId, params: MacdParams): Void? {
        return strategyParamsDao.insert(MacdParamsEntity.of(strategyValuesId, params))
    }
}
