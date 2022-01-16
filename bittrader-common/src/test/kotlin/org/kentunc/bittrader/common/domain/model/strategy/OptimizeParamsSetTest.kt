package org.kentunc.bittrader.common.domain.model.strategy

import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.strategy.params.OptimizeParamsSet
import org.kentunc.bittrader.common.domain.model.strategy.params.bbands.BBandsParams
import org.kentunc.bittrader.common.domain.model.strategy.params.rsi.RsiParams

internal class OptimizeParamsSetTest {

    @Test
    fun testProduct() {
        // setup:
        val rsiParamsList = listOf<RsiParams>(mockk(), mockk())
        val bBandsParamsList = listOf<BBandsParams>(mockk(), mockk(), mockk())
        val expectedSize = rsiParamsList.size * bBandsParamsList.size

        // exercise:
        val actualSize = OptimizeParamsSet.product(rsiParamsList, bBandsParamsList).size

        // verify:
        assertEquals(expectedSize, actualSize)
    }
}
