package org.kentunc.bittrader.candle.api.infrastructure.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kentunc.bittrader.candle.api.domain.repository.CandleRepository
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.CandleDao
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.CandleEntity
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.CandlePrimaryKey
import org.kentunc.bittrader.common.domain.model.candle.Candle
import org.kentunc.bittrader.common.domain.model.candle.CandleId
import org.kentunc.bittrader.common.domain.model.candle.CandleQuery
import org.springframework.stereotype.Repository

@Repository
class CandleRepositoryImpl(private val candleDao: CandleDao) : CandleRepository {

    override suspend fun findOne(candleId: CandleId): Candle? {
        return candleDao.selectOne(CandlePrimaryKey.of(candleId))?.toCandle()
    }

    override suspend fun find(query: CandleQuery): Flow<Candle> {
        return candleDao.select(query)
            .map { it.toCandle() }
    }

    override suspend fun save(candle: Candle): Void? {
        return candleDao.insert(CandleEntity.of(candle))
    }

    override suspend fun update(candle: Candle): Void? {
        return candleDao.update(CandleEntity.of(candle))
    }
}
