package org.kentunc.bittrader.candle.api.domain.repository

import org.kentunc.bittrader.common.domain.model.candle.Candle
import org.kentunc.bittrader.common.domain.model.candle.CandleId

interface CandleRepository {

    suspend fun find(candleId: CandleId): Candle?

    suspend fun save(candle: Candle): Void?

    suspend fun update(candle: Candle): Void?
}
