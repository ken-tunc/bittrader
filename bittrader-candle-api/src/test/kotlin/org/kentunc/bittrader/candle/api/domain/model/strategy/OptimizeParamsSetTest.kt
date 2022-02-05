package org.kentunc.bittrader.candle.api.domain.model.strategy

import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.candle.api.domain.model.strategy.params.BBandsParams
import org.kentunc.bittrader.candle.api.domain.model.strategy.params.MacdParams

internal class OptimizeParamsSetTest {

    @Test
    fun testProduct() {
        // setup:
        val macdParamsList = listOf<MacdParams>(mockk(), mockk())
        val bBandsParamsList = listOf<BBandsParams>(mockk(), mockk(), mockk())
        val expectedSize = macdParamsList.size * bBandsParamsList.size

        // exercise:
        val actualSize = OptimizeParamsSet.product(macdParamsList, bBandsParamsList).size

        // verify:
        assertEquals(expectedSize, actualSize)
    }
}
