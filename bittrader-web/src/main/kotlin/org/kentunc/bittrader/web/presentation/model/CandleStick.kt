package org.kentunc.bittrader.web.presentation.model

import org.kentunc.bittrader.common.domain.model.candle.Candle
import java.math.BigDecimal
import java.time.LocalDateTime

data class CandleStick(val dateTime: LocalDateTime, val quotes: List<BigDecimal>) {

    companion object {
        fun of(candle: Candle) = CandleStick(
            dateTime = candle.id.dateTime.toLocalDateTime(),
            quotes = listOf(
                candle.open.toBigDecimal(),
                candle.close.toBigDecimal(),
                candle.high.toBigDecimal(),
                candle.low.toBigDecimal()
            )
        )
    }
}
