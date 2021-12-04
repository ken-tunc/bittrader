package org.kentunc.bittrader.candle.feeder.presentation.event

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.shared.annotation.Generated
import org.springframework.context.ApplicationEvent

class CandleFeedEvent(source: Any, val productCode: ProductCode) : ApplicationEvent(source) {
    @Generated
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CandleFeedEvent

        if (productCode != other.productCode) return false

        return true
    }

    @Generated
    override fun hashCode(): Int {
        return productCode.hashCode()
    }
}
