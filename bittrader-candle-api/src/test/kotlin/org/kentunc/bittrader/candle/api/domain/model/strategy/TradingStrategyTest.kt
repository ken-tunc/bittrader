package org.kentunc.bittrader.candle.api.domain.model.strategy

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.kentunc.bittrader.candle.api.domain.model.candle.toBarSeries
import org.kentunc.bittrader.candle.api.domain.model.strategy.params.BBandsParams
import org.kentunc.bittrader.candle.api.domain.model.strategy.params.MacdParams
import org.kentunc.bittrader.common.domain.model.candle.CandleList
import org.kentunc.bittrader.common.domain.model.strategy.TradingPosition
import org.ta4j.core.BarSeries
import org.ta4j.core.BarSeriesManager
import org.ta4j.core.BaseStrategy
import org.ta4j.core.TradingRecord
import org.ta4j.core.analysis.criteria.pnl.GrossProfitCriterion
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

        val macdParams = mockk<MacdParams>(relaxed = true)
        val bBandsParams = mockk<BBandsParams>(relaxed = true)

        // exercise:
        val actual = TradingStrategy.of(candleList, macdParams, bBandsParams).getPosition()

        // verify:
        assertEquals(expected, actual)
    }

    @Test
    fun testGetProfit() {
        // setup:
        val candleList = mockk<CandleList>()
        val barSeries = mockk<BarSeries>(relaxed = true)
        val endIndex = 1
        every { barSeries.endIndex } returns endIndex

        mockkStatic("org.kentunc.bittrader.candle.api.domain.model.candle.CandleListExtensionsKt")
        every { candleList.toBarSeries() } returns barSeries

        val profit = mockk<Num>()
        val tradingRecord = mockk<TradingRecord>()
        mockkConstructor(BarSeriesManager::class)
        every { anyConstructed<BarSeriesManager>().run(any()) } returns tradingRecord
        mockkConstructor(GrossProfitCriterion::class)
        every { anyConstructed<GrossProfitCriterion>().calculate(barSeries, tradingRecord) } returns profit

        val macdParams = mockk<MacdParams>(relaxed = true)
        val bBandsParams = mockk<BBandsParams>(relaxed = true)

        // exercise:
        val actual = TradingStrategy.of(candleList, macdParams, bBandsParams).getProfit()

        // verify:
        assertEquals(profit, actual)
    }
}
