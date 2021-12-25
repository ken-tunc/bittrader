package org.kentunc.bittrader.common.domain.model.quote

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.math.BigDecimal

internal class SizeTest {

    @ParameterizedTest
    @ValueSource(doubles = [0.0, 100.00001])
    fun instantiate(value: Double) {
        val size = Size.of(value)
        assertTrue(BigDecimal.valueOf(value).compareTo(size.toBigDecimal()) == 0)
    }

    @Test
    fun invalidValue() {
        assertThrows<IllegalArgumentException> { Size.of(-1.0) }
    }

    @Test
    fun testAdd() {
        val size = Size.of(100.0)
        val augend = Size.of(234.567)
        val expected = Size.of(334.567)
        assertEquals(expected, size + augend)
    }

    @Test
    fun testMinus() {
        val size = Size.of(1000.0)
        val subtrahend = Size.of(234.56789)
        val expected = Size.of(765.43211)
        assertEquals(expected, size - subtrahend)
    }
}
