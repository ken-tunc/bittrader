package org.kentunc.bittrader.candle.api.domain.model.strategy

import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.candle.api.domain.model.strategy.params.MacdParams
import org.kentunc.bittrader.candle.api.domain.model.strategy.params.RsiParams

internal class OptimizeParamsSetTest {

    @Test
    fun testProduct() {
        // setup:
        val macdParamsList = listOf<MacdParams>(mockk(), mockk())
        val rsiParamsList = listOf<RsiParams>(mockk(), mockk(), mockk())
        val expectedSize = macdParamsList.size * rsiParamsList.size

        // exercise:
        val actualSize = OptimizeParamsSet.product(macdParamsList, rsiParamsList).size

        // verify:
        assertEquals(expectedSize, actualSize)
    }
}
