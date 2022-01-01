package org.kentunc.bittrader.candle.api.domain.model.strategy.ema

import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class EmaParamsTest {

    @ParameterizedTest
    @CsvSource("0,1", "1,0", "1,1", "2,1")
    fun instantiate_invalid(shortTimeFrame: Int, longTimeFrame: Int) {
        assertThrows<IllegalArgumentException> { EmaParams(shortTimeFrame, longTimeFrame) }
    }
}
