package org.kentunc.bittrader.candle.api.domain.model.strategy.params

import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class RsiParamsTest {

    @ParameterizedTest
    @CsvSource("0,70,30", "1,69,30", "1,70,29")
    fun instantiate_invalid(timeFrame: Int, buyThreshold: Int, sellThreshold: Int) {
        assertThrows<IllegalArgumentException> { RsiParams(timeFrame, buyThreshold, sellThreshold) }
    }
}
