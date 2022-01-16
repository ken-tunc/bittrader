package org.kentunc.bittrader.common.domain.model.strategy.params

import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.kentunc.bittrader.common.domain.model.strategy.params.rsi.RsiParams
import org.kentunc.bittrader.common.domain.model.strategy.params.rsi.RsiThreshold

internal class RsiParamsTest {

    @ParameterizedTest
    @CsvSource("0,70,30", "1,69,30", "1,70,29")
    fun instantiate_invalid(timeFrame: Int, buyThreshold: Int, sellThreshold: Int) {
        assertThrows<IllegalArgumentException> {
            RsiParams(
                timeFrame = TimeFrame.of(timeFrame),
                buyThreshold = RsiThreshold.of(buyThreshold),
                sellThreshold = RsiThreshold.of(sellThreshold)
            )
        }
    }
}
