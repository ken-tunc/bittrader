package org.kentunc.bittrader.common.domain.model.order

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class MinutesToExpireTest {

    @ParameterizedTest
    @ValueSource(ints = [0, 1, Int.MAX_VALUE])
    fun instantiate(value: Int) {
        val size = MinutesToExpire.of(value)
        assertEquals(value, size.toInt())
    }

    @Test
    fun invalidValue() {
        assertThrows<IllegalArgumentException> { MinutesToExpire.of(-1) }
    }
}
