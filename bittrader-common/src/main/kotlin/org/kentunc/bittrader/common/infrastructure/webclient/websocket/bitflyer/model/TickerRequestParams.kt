package org.kentunc.bittrader.common.infrastructure.webclient.websocket.bitflyer.model

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.shared.annotation.Generated

class TickerRequestParams(productCode: ProductCode) {

    val channel = "lightning_ticker_$productCode"

    @Generated
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TickerRequestParams

        if (channel != other.channel) return false

        return true
    }

    @Generated
    override fun hashCode(): Int {
        return channel.hashCode()
    }
}
