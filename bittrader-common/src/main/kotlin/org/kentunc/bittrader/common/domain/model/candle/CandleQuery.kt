package org.kentunc.bittrader.common.domain.model.candle

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration

data class CandleQuery(val productCode: ProductCode, val duration: Duration, val maxNum: Int?)
