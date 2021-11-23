package org.kentunc.bittrader.common.domain.model.quote

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.math.BigDecimal

internal class VolumeTest {

    @ParameterizedTest
    @ValueSource(doubles = [0.0, 100.00001])
    fun instantiate(value: Double) {
        val volume = Volume.of(value)
        assertTrue(BigDecimal.valueOf(value).compareTo(volume.toBigDecimal()) == 0)
    }

    @Test
    fun invalidValue() {
        assertThrows<IllegalArgumentException> { Volume.of(-1.0) }
    }

    @Test
    fun testAdd() {
        val volume = Volume.of(100.0)
        val augend = Volume.of(234.567)
        val expected = Volume.of(334.567)
        assertEquals(expected, volume + augend)
    }
}
