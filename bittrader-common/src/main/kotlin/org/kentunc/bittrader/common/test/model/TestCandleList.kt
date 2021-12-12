package org.kentunc.bittrader.common.test.model

import org.kentunc.bittrader.common.domain.model.candle.CandleList
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration
import java.time.LocalDateTime

object TestCandleList {

    fun create(): CandleList {
        val baseDateTime = LocalDateTime.of(2021, 1, 1, 12, 0, 0)
        return CandleList.of(
            listOf(
                TestCandle.create(
                    productCode = ProductCode.BTC_JPY,
                    duration = Duration.MINUTES,
                    dateTime = baseDateTime
                ),
                TestCandle.create(
                    productCode = ProductCode.BTC_JPY,
                    duration = Duration.MINUTES,
                    dateTime = baseDateTime.plusMinutes(1)
                ),
                TestCandle.create(
                    productCode = ProductCode.BTC_JPY,
                    duration = Duration.MINUTES,
                    dateTime = baseDateTime.plusMinutes(2)
                ),
            )
        )
    }
}
