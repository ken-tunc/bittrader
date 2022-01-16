package org.kentunc.bittrader.common.domain.model.strategy.params

import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.kentunc.bittrader.common.domain.model.strategy.params.macd.MacdParams

internal class MacdParamsTest {

    @ParameterizedTest
    @CsvSource("0,1,1", "1,0,1", "1,2,0", "1,1,1")
    fun instantiate_invalid(shortTimeFrame: Int, longTimeFrame: Int, signalTimeFrame: Int) {
        assertThrows<IllegalArgumentException> {
            MacdParams(
                shortTimeFrame = TimeFrame.of(shortTimeFrame),
                longTimeFrame = TimeFrame.of(longTimeFrame),
                signalTimeFrame = TimeFrame.of(signalTimeFrame)
            )
        }
    }
}
