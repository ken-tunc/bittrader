package org.kentunc.bittrader.order.scheduler.domain.repository

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration

interface CandleRepository {

    suspend fun getCandleList(productCode: ProductCode, duration: Duration, maxNum: Int)
}
