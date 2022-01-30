package org.kentunc.bittrader.common.domain.model.strategy.params

import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.kentunc.bittrader.common.domain.model.strategy.params.bbands.BBandsK
import org.kentunc.bittrader.common.domain.model.strategy.params.bbands.BBandsParams

internal class BBandsParamsTest {

    @ParameterizedTest
    @CsvSource("0,2.0,1.0", "20,0,1.0", "20,3.1,1.0", "20,2.0,0", "20,2.0,3.1", "20,1.0,2.0")
    fun instantiate_invalid(timeFrame: Int, buyK: Double, sellK: Double) {
        assertThrows<IllegalArgumentException> {
            BBandsParams(
                timeFrame = TimeFrame.of(timeFrame),
                buyK = BBandsK.of(buyK),
                sellK = BBandsK.of(sellK)
            )
        }
    }
}
