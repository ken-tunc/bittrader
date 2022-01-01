package org.kentunc.bittrader.candle.api.domain.service

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkConstructor
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.candle.api.domain.model.candle.toBarSeries
import org.kentunc.bittrader.candle.api.domain.model.strategy.IndicatorRuleSet
import org.kentunc.bittrader.candle.api.domain.model.strategy.StrategyParams
import org.kentunc.bittrader.candle.api.domain.model.strategy.StrategyValues
import org.kentunc.bittrader.candle.api.domain.model.strategy.StrategyValuesId
import org.kentunc.bittrader.candle.api.domain.repository.StrategyParamsRepository
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.common.test.model.TestCandleList
import org.ta4j.core.BarSeries
import org.ta4j.core.BarSeriesManager
import org.ta4j.core.Strategy
import org.ta4j.core.TradingRecord
import org.ta4j.core.analysis.criteria.pnl.GrossProfitCriterion
import org.ta4j.core.num.Num

internal class AbstractStrategyServiceTest {

    @RelaxedMockK
    private lateinit var ruleSet: IndicatorRuleSet
    @RelaxedMockK
    private lateinit var strategy: Strategy
    @RelaxedMockK
    private lateinit var strategyParamsRepository: StrategyParamsRepository<StrategyParams>

    private lateinit var target: AbstractStrategyService<StrategyParams>

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        target = object : AbstractStrategyService<StrategyParams>(strategyParamsRepository) {
            override suspend fun buildRuleSet(barSeries: BarSeries, params: StrategyParams): IndicatorRuleSet {
                return ruleSet
            }
        }
    }

    @Test
    fun testGetRuleSet() = runBlocking {
        // setup:
        val candleList = TestCandleList.create()
        val id = StrategyValuesId(ProductCode.BTC_JPY, Duration.DAYS)
        val params = mockk<StrategyParams>()
        coEvery { strategyParamsRepository.get(id) } returns StrategyValues.of(id, params)

        // exercise:
        val actual = target.getRuleSet(candleList.toBarSeries(), id)

        // verify:
        assertEquals(ruleSet, actual)
    }

    @Test
    fun testOptimize_update() = runBlocking {
        // setup:
        val candleList = TestCandleList.create()
        val id = StrategyValuesId(ProductCode.BTC_JPY, Duration.DAYS)
        val optimized = mockk<StrategyParams>()
        every { strategyParamsRepository.getForOptimize() } returns flowOf(optimized)

        val record = mockk<TradingRecord>()
        mockkConstructor(BarSeriesManager::class)
        every { anyConstructed<BarSeriesManager>().run(strategy) } returns record

        val profit = mockk<Num>(relaxed = true)
        mockkConstructor(GrossProfitCriterion::class)
        every { anyConstructed<GrossProfitCriterion>().calculate(any(), record) } returns profit

        val current = mockk<StrategyParams>()
        coEvery { strategyParamsRepository.get(id) } returns StrategyValues.of(id, current)

        // exercise:
        target.optimize(candleList.toBarSeries(), id)

        // verify:
        coVerify { strategyParamsRepository.save(id, optimized) }
    }

    @Test
    fun testOptimize_no_update() = runBlocking {
        // setup:
        val candleList = TestCandleList.create()
        val id = StrategyValuesId(ProductCode.BTC_JPY, Duration.DAYS)
        val current = mockk<StrategyParams>()
        every { strategyParamsRepository.getForOptimize() } returns flowOf(current)

        val record = mockk<TradingRecord>()
        mockkConstructor(BarSeriesManager::class)
        every { anyConstructed<BarSeriesManager>().run(strategy) } returns record

        val profit = mockk<Num>(relaxed = true)
        mockkConstructor(GrossProfitCriterion::class)
        every { anyConstructed<GrossProfitCriterion>().calculate(any(), record) } returns profit

        coEvery { strategyParamsRepository.get(id) } returns StrategyValues.of(id, current)

        // exercise:
        target.optimize(candleList.toBarSeries(), id)

        // verify:
        coVerify(exactly = 0) { strategyParamsRepository.save(id, current) }
    }
}
