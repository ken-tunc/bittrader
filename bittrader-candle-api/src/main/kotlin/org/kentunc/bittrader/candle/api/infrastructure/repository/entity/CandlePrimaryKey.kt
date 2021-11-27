package org.kentunc.bittrader.candle.api.infrastructure.repository.entity

import org.kentunc.bittrader.common.domain.model.candle.CandleId
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration
import java.time.LocalDateTime

data class CandlePrimaryKey(
    val productCode: ProductCode,
    val duration: Duration,
    val dateTime: LocalDateTime
) {

    companion object {
        fun of(id: CandleId) = CandlePrimaryKey(id.productCode, id.duration, id.dateTime.toLocalDateTime())
    }
}
