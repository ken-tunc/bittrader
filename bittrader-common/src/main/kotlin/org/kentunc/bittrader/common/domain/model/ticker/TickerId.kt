package org.kentunc.bittrader.common.domain.model.ticker

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.DateTime

data class TickerId(val productCode: ProductCode, val dateTime: DateTime)
