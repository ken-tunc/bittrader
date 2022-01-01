package org.kentunc.bittrader.candle.api.domain.model

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.kentunc.bittrader.candle.api.domain.model.strategy.IndicatorRuleSet
import org.kentunc.bittrader.common.domain.model.strategy.TradingPosition
import org.ta4j.core.BarSeries
import org.ta4j.core.BaseStrategy
import org.ta4j.core.rules.BooleanRule

internal class TradingStrategyTest {

    @ParameterizedTest
    @CsvSource(
        "true,false,SHOULD_BUY",
        "false,true,SHOULD_SELL",
        "false,false,NEUTRAL",
    )
    fun testPosition(shouldEnter: Boolean, shouldExit: Boolean, expected: TradingPosition) {
        // setup:
        val mockRule = BooleanRule.TRUE
        val indicatorRuleSet1 = mockk<IndicatorRuleSet>()
        every { indicatorRuleSet1.entryRule } returns mockRule
        every { indicatorRuleSet1.exitRule } returns mockRule
        val indicatorRuleSet2 = mockk<IndicatorRuleSet>()
        every { indicatorRuleSet2.entryRule } returns mockRule
        every { indicatorRuleSet2.exitRule } returns mockRule

        val endIndex = 100
        val barSeries = mockk<BarSeries>()
        every { barSeries.endIndex } returns endIndex

        mockkConstructor(BaseStrategy::class)
        every { anyConstructed<BaseStrategy>().shouldEnter(endIndex) } returns shouldEnter
        every { anyConstructed<BaseStrategy>().shouldExit(endIndex) } returns shouldExit

        // exercise:
        val actual = TradingStrategy.of(listOf(indicatorRuleSet1, indicatorRuleSet2), barSeries).position

        // verify:
        assertEquals(expected, actual)
    }
}
