package org.kentunc.bittrader.candle.feeder.presentation.event

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.springframework.context.ApplicationEvent

data class CandleFeedEvent(val productCode: ProductCode) : ApplicationEvent(productCode)
