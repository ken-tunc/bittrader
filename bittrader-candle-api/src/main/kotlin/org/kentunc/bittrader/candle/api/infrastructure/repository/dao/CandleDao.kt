package org.kentunc.bittrader.candle.api.infrastructure.repository.dao

import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.CandleEntity
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.CandlePrimaryKey
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.criteria
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.update
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.r2dbc.core.insert
import org.springframework.data.r2dbc.core.select
import org.springframework.data.r2dbc.core.update
import org.springframework.data.relational.core.query.Query
import org.springframework.stereotype.Component

@Component
class CandleDao(private val template: R2dbcEntityTemplate) {

    suspend fun find(primaryKey: CandlePrimaryKey): CandleEntity? {
        return template.select<CandleEntity>()
            .matching(Query.query(primaryKey.criteria()))
            .one()
            .awaitSingleOrNull()
    }

    suspend fun save(candleEntity: CandleEntity): Void? {
        return template.insert<CandleEntity>()
            .using(candleEntity)
            .then()
            .awaitSingleOrNull()
    }

    suspend fun update(candleEntity: CandleEntity): Void? {
        return template.update<CandleEntity>()
            .matching(Query.query(candleEntity.primaryKey.criteria()))
            .apply(candleEntity.update())
            .then()
            .awaitSingleOrNull()
    }
}
