package org.kentunc.bittrader.common.domain.model.strategy

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.kentunc.bittrader.candle.api.domain.model.candle.toBarSeries
import org.kentunc.bittrader.common.domain.model.candle.CandleList
import org.kentunc.bittrader.common.domain.model.strategy.params.bbands.BBandsParams
import org.kentunc.bittrader.common.domain.model.strategy.params.rsi.RsiParams
import org.ta4j.core.BarSeries
import org.ta4j.core.BarSeriesManager
import org.ta4j.core.BaseStrategy
import org.ta4j.core.TradingRecord
import org.ta4j.core.analysis.criteria.pnl.ProfitLossRatioCriterion
import org.ta4j.core.num.Num

internal class TradingStrategyTest {

    @ParameterizedTest
    @CsvSource("true,true,SHOULD_BUY", "false,true,SHOULD_SELL", "false,false,NEUTRAL")
    fun testGetPosition(shouldEnter: Boolean, shouldExit: Boolean, expected: TradingPosition) {
        // setup:
        val candleList = mockk<CandleList>()
        val barSeries = mockk<BarSeries>(relaxed = true)
        val endIndex = 1
        every { barSeries.endIndex } returns endIndex

        mockkStatic("org.kentunc.bittrader.candle.api.domain.model.candle.CandleListExtensionsKt")
        every { candleList.toBarSeries() } returns barSeries

        mockkConstructor(BaseStrategy::class)
        every { anyConstructed<BaseStrategy>().shouldEnter(endIndex) } returns shouldEnter
        every { anyConstructed<BaseStrategy>().shouldExit(endIndex) } returns shouldExit

        val rsiParams = mockk<RsiParams>(relaxed = true)
        val bBandsParams = mockk<BBandsParams>(relaxed = true)

        // exercise:
        val actual = TradingStrategy.of(candleList, rsiParams, bBandsParams).getPosition()

        // verify:
        assertEquals(expected, actual)
    }

    @Test
    fun testGetCriterionValue() {
        // setup:
        val candleList = mockk<CandleList>()
        val barSeries = mockk<BarSeries>(relaxed = true)
        val endIndex = 1
        every { barSeries.endIndex } returns endIndex

        mockkStatic("org.kentunc.bittrader.candle.api.domain.model.candle.CandleListExtensionsKt")
        every { candleList.toBarSeries() } returns barSeries

        val profit = mockk<Num>()
        val tradingRecord = mockk<TradingRecord>(relaxed = true)
        mockkConstructor(BarSeriesManager::class)
        every { anyConstructed<BarSeriesManager>().run(any()) } returns tradingRecord
        mockkConstructor(ProfitLossRatioCriterion::class)
        every { anyConstructed<ProfitLossRatioCriterion>().calculate(barSeries, tradingRecord) } returns profit

        val rsiParams = mockk<RsiParams>(relaxed = true)
        val bBandsParams = mockk<BBandsParams>(relaxed = true)

        // exercise:
        val actual = TradingStrategy.of(candleList, rsiParams, bBandsParams).getCriterionValue()

        // verify:
        assertEquals(profit, actual)
    }
}
