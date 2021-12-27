package org.kentunc.bittrader.candle.api.infrastructure.repository.dao

import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.StrategyParamsEntity
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.StrategyParamsPrimaryKey

suspend inline fun <reified T : StrategyParamsEntity> StrategyParamsDao.selectLatestOne(primaryKey: StrategyParamsPrimaryKey): T? =
    selectLatestOne(primaryKey, T::class.java)

suspend inline fun <reified T : StrategyParamsEntity> StrategyParamsDao.insert(entity: T): Void? =
    insert(entity, T::class.java)
