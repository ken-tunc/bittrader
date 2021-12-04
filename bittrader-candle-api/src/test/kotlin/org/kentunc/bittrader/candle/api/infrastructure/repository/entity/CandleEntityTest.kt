package org.kentunc.bittrader.candle.api.infrastructure.repository.entity

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.test.model.TestCandle

internal class CandleEntityTest {

    @Test
    fun testToCandle() {
        // setup:
        val expected = TestCandle.create()
        val entity = CandleEntity(
            productCode = expected.id.productCode,
            duration = expected.id.duration,
            dateTime = expected.id.dateTime.toLocalDateTime(),
            open = expected.open.toBigDecimal(),
            close = expected.close.toBigDecimal(),
            high = expected.high.toBigDecimal(),
            low = expected.low.toBigDecimal(),
            volume = expected.volume.toBigDecimal()
        )

        // exercise:
        val actual = entity.toCandle()

        // verify:
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
