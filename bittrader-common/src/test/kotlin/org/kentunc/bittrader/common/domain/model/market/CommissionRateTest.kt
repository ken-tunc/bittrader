package org.kentunc.bittrader.common.domain.model.market

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.kentunc.bittrader.common.domain.model.quote.Size
import java.math.BigDecimal

internal class CommissionRateTest {

    @ParameterizedTest
    @ValueSource(doubles = [0.0, 100.00001])
    fun instantiate(value: Double) {
        val commissionRate = CommissionRate.of(value)
        assertTrue(BigDecimal.valueOf(value).compareTo(commissionRate.toBigDecimal()) == 0)
    }

    @Test
    fun invalidValue() {
        assertThrows<IllegalArgumentException> { CommissionRate.of(-1.0) }
    }

    @Test
    fun testFee() {
        val size = Size.Companion.of(123.45)
        val commissionRate = CommissionRate.of(BigDecimal("0.015"))
        val expected = Size.of(BigDecimal("1.85175"))
        assertEquals(expected, commissionRate.fee(size))
    }
}
