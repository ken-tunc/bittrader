package org.kentunc.bittrader.common.domain.model.candle

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.common.domain.model.time.TruncatedDateTime

data class CandleId(val productCode: ProductCode, val duration: Duration, val dateTime: TruncatedDateTime)
