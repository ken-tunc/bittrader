package org.kentunc.bittrader.web.domain.repository

import org.kentunc.bittrader.common.domain.model.candle.CandleList
import org.kentunc.bittrader.common.domain.model.candle.CandleQuery

interface CandleRepository {

    suspend fun search(query: CandleQuery): CandleList
}
