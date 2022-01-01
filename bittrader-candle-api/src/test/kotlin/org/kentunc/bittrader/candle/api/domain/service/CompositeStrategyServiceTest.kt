package org.kentunc.bittrader.candle.api.domain.service

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.candle.api.domain.model.TradingStrategy
import org.kentunc.bittrader.candle.api.domain.model.candle.toBarSeries
import org.kentunc.bittrader.candle.api.domain.model.strategy.IndicatorRuleSet
import org.kentunc.bittrader.candle.api.domain.model.strategy.StrategyValuesId
import org.kentunc.bittrader.common.domain.model.candle.CandleList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.ta4j.core.BarSeries

@SpringJUnitConfig(CompositeStrategyService::class)
internal class CompositeStrategyServiceTest {

    @MockkBean(relaxed = true)
    private lateinit var emaStrategyService: EmaStrategyService

    @Autowired
    private lateinit var target: CompositeStrategyService

    @Test
    fun testGetStrategy() = runBlocking {
        // setup:
        val barSeries = mockk<BarSeries>()
        val candleList = mockk<CandleList>()
        mockkStatic("org.kentunc.bittrader.candle.api.domain.model.candle.CandleListExtensionsKt")
        every { candleList.toBarSeries() } returns barSeries

        val strategyValuesId = mockk<StrategyValuesId>()
        val ruleSet = mockk<IndicatorRuleSet>()
        coEvery { emaStrategyService.getRuleSet(barSeries, strategyValuesId) } returns ruleSet

        val strategy = mockk<TradingStrategy>()
        mockkObject(TradingStrategy)
        every { TradingStrategy.of(listOf(ruleSet), barSeries) } returns strategy

        // exercise:
        val actual = target.getStrategy(candleList, strategyValuesId)

        // verify:
        assertEquals(strategy, actual)
    }

    @Test
    fun testOptimize() = runBlocking {
        // setup:
        val barSeries = mockk<BarSeries>()
        val candleList = mockk<CandleList>()
        mockkStatic("org.kentunc.bittrader.candle.api.domain.model.candle.CandleListExtensionsKt")
        every { candleList.toBarSeries() } returns barSeries
        val strategyValuesId = mockk<StrategyValuesId>()

        // exercise:
        target.optimize(candleList, strategyValuesId)

        // verify:
        coVerify { emaStrategyService.optimize(barSeries, strategyValuesId) }
    }
}
