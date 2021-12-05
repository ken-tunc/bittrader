package org.kentunc.bittrader.common.presentation.model.candle

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.test.model.TestCandle

internal class CandleResponseTest {

    @Test
    fun testOf() {
        val candle = TestCandle.create()
        val actual = CandleResponse.of(candle)

        assertAll(
            { assertEquals(candle.id.productCode, actual.productCode) },
            { assertEquals(candle.id.duration, actual.duration) },
            { assertEquals(candle.id.dateTime.toLocalDateTime(), actual.dateTime) },
            { assertEquals(candle.open.toBigDecimal(), actual.open) },
            { assertEquals(candle.close.toBigDecimal(), actual.close) },
            { assertEquals(candle.high.toBigDecimal(), actual.high) },
            { assertEquals(candle.low.toBigDecimal(), actual.low) },
            { assertEquals(candle.volume.toBigDecimal(), actual.volume) },
        )
    }

    @Test
    fun testToCandle() {
        val expected = TestCandle.create()
        val response = CandleResponse.of(expected)

        val actual = response.toCandle()
        assertAll(
            { assertEquals(expected.id, actual.id) },
            { assertEquals(expected.open, actual.open) },
            { assertEquals(expected.close, actual.close) },
            { assertEquals(expected.high, actual.high) },
            { assertEquals(expected.low, actual.low) },
            { assertEquals(expected.volume, actual.volume) },
        )
    }
}
