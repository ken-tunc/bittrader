package org.kentunc.bittrader.candle.api.infrastructure.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.kentunc.bittrader.candle.api.domain.repository.BBandsParamsRepository
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.StrategyParamsDao
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.insert
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.selectLatestOne
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.BBandsParamsEntity
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.StrategyParamsPrimaryKey
import org.kentunc.bittrader.common.domain.model.strategy.params.StrategyValues
import org.kentunc.bittrader.common.domain.model.strategy.params.StrategyValuesId
import org.kentunc.bittrader.common.domain.model.strategy.params.bbands.BBandsParams

class BBandsParamsRepositoryImpl(
    private val defaultParams: BBandsParams,
    private val paramsForOptimize: List<BBandsParams>,
    private val strategyParamsDao: StrategyParamsDao
) : BBandsParamsRepository {

    override suspend fun get(strategyValuesId: StrategyValuesId): StrategyValues<BBandsParams> {
        val primaryKey = StrategyParamsPrimaryKey.of(strategyValuesId)
        return strategyParamsDao.selectLatestOne<BBandsParamsEntity>(primaryKey)
            ?.toStrategyValues()
            ?: StrategyValues.of(strategyValuesId, defaultParams)
    }

    override fun getForOptimize(): Flow<BBandsParams> {
        return paramsForOptimize.asFlow()
    }

    override suspend fun save(strategyValuesId: StrategyValuesId, params: BBandsParams): Void? {
        return strategyParamsDao.insert(BBandsParamsEntity.of(strategyValuesId, params))
    }
}
