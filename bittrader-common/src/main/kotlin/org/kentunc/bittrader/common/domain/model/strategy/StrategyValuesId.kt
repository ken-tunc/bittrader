package org.kentunc.bittrader.common.domain.model.strategy

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration

data class StrategyValuesId(val productCode: ProductCode, val duration: Duration)
