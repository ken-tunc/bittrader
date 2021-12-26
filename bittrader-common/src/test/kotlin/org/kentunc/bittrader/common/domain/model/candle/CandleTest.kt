package org.kentunc.bittrader.common.domain.model.candle

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.quote.Price
import org.kentunc.bittrader.common.domain.model.quote.Volume
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.common.domain.model.time.TruncatedDateTime
import org.kentunc.bittrader.common.test.model.TestCandle
import org.kentunc.bittrader.common.test.model.TestTicker
import java.time.LocalDateTime

internal class CandleTest {

    @Test
    fun instantiate() {
        val ticker = TestTicker.create()
        val candle = Candle.of(ticker, Duration.DAYS)
        assertAll(
            { assertEquals(ticker.id.productCode, candle.id.productCode) },
            { assertEquals(ticker.volume, candle.volume) }
        )
    }

    @Test
    fun `invalid argument - high less than low`() {
        val duration = Duration.DAYS
        assertThrows(IllegalArgumentException::class.java) {
            Candle.of(
                id = CandleId(
                    ProductCode.BTC_JPY,
                    duration,
                    TruncatedDateTime.Companion.of(LocalDateTime.now(), duration)
                ),
                open = Price.of(100.0),
                close = Price.of(100.0),
                high = Price.of(100.0),
                low = Price.of(100.01),
                volume = Volume.of(100.0)
            )
        }
    }

    @Test
    fun testExtendWith() {
        // setup:
        val localDateTime = LocalDateTime.of(2020, 1, 1, 0, 0, 0)
        val candle = TestCandle.create(
            dateTime = localDateTime,
            open = 100.0,
            close = 200.0,
            high = 300.0,
            low = 50.0,
            volume = 100.0
        )
        val ticker = TestTicker.create(
            dateTime = localDateTime,
            bestBid = 400.0,
            bestAsk = 400.0,
            volume = 150.0
        )

        // exercise
        val actual = candle.extendWith(ticker)

        // verify
        assertAll(
            { assertEquals(ProductCode.BTC_JPY, actual.id.productCode) },
            { assertEquals(Duration.DAYS, actual.id.duration) },
            { assertEquals(Price.of(100.0), actual.open) },
            { assertEquals(Price.of(400.0), actual.close) },
            { assertEquals(Price.of(400.0), actual.high) },
            { assertEquals(Price.of(50.0), actual.low) },
            { assertEquals(Volume.of(250.0), actual.volume) }
        )
    }

    @Test
    fun `test extendWith() with invalid product code`() {
        val localDateTime = LocalDateTime.of(2020, 1, 1, 0, 0, 0)
        val candle = TestCandle.create(productCode = ProductCode.BTC_JPY, dateTime = localDateTime)
        val ticker = TestTicker.create(productCode = ProductCode.ETH_JPY, dateTime = localDateTime)

        assertThrows(IllegalArgumentException::class.java) {
            candle.extendWith(ticker)
        }
    }

    @Test
    fun `test extendWith() with invalid datetime`() {
        val localDateTime = LocalDateTime.of(2020, 1, 1, 0, 0, 0)
        val candle = TestCandle.create(dateTime = localDateTime)
        val ticker = TestTicker.create(dateTime = localDateTime.plusDays(1))

        assertThrows(IllegalArgumentException::class.java) {
            candle.extendWith(ticker)
        }
    }
}
