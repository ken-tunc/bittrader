package org.kentunc.bittrader.candle.api.domain.repository

import kotlinx.coroutines.flow.Flow
import org.kentunc.bittrader.common.domain.model.candle.Candle
import org.kentunc.bittrader.common.domain.model.candle.CandleId
import org.kentunc.bittrader.common.domain.model.candle.CandleQuery

interface CandleRepository {

    suspend fun findOne(candleId: CandleId): Candle?

    suspend fun find(query: CandleQuery): Flow<Candle>

    suspend fun save(candle: Candle): Void?

    suspend fun update(candle: Candle): Void?
}
