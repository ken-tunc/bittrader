package org.kentunc.bittrader.common.domain.model.strategy.params

import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.kentunc.bittrader.common.domain.model.strategy.params.ema.EmaParams

internal class EmaParamsTest {

    @ParameterizedTest
    @CsvSource("0,1", "1,0", "1,1", "2,1")
    fun instantiate_invalid(shortTimeFrame: Int, longTimeFrame: Int) {
        assertThrows<IllegalArgumentException> {
            EmaParams(
                shortTimeFrame = TimeFrame.of(shortTimeFrame),
                longTimeFrame = TimeFrame.of(longTimeFrame)
            )
        }
    }
}
