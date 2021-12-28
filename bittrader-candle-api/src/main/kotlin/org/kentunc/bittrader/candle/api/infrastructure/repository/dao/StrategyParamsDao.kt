package org.kentunc.bittrader.candle.api.infrastructure.repository.dao

import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.StrategyParamsEntity
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.StrategyParamsPrimaryKey
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.latest
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Component

@Component
class StrategyParamsDao(private val template: R2dbcEntityTemplate) {

    suspend fun <T : StrategyParamsEntity> selectLatestOne(
        primaryKey: StrategyParamsPrimaryKey,
        entityType: Class<T>
    ): T? {
        return template.select(entityType)
            .matching(primaryKey.latest())
            .first()
            .awaitSingleOrNull()
    }

    suspend fun <T : StrategyParamsEntity> insert(entity: T, entityType: Class<T>): Void? {
        return template.insert(entityType)
            .using(entity)
            .then()
            .awaitSingleOrNull()
    }
}
