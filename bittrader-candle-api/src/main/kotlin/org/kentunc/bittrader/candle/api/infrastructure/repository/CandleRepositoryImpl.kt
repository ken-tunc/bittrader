package org.kentunc.bittrader.candle.api.infrastructure.repository

import org.kentunc.bittrader.candle.api.domain.repository.CandleRepository
import org.kentunc.bittrader.candle.api.infrastructure.repository.dao.CandleDao
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.CandleEntity
import org.kentunc.bittrader.candle.api.infrastructure.repository.entity.CandlePrimaryKey
import org.kentunc.bittrader.common.domain.model.candle.Candle
import org.kentunc.bittrader.common.domain.model.candle.CandleId
import org.springframework.stereotype.Repository

@Repository
class CandleRepositoryImpl(private val candleDao: CandleDao) : CandleRepository {

    override suspend fun find(candleId: CandleId): Candle? {
        return candleDao.find(CandlePrimaryKey.of(candleId))?.toCandle()
    }

    override suspend fun save(candle: Candle): Void? {
        return candleDao.save(CandleEntity.of(candle))
    }

    override suspend fun update(candle: Candle): Void? {
        return candleDao.update(CandleEntity.of(candle))
    }
}
