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
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.kentunc.bittrader.candle.api.domain.repository.StrategyParamsRepository
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.strategy.StrategyParams
import org.kentunc.bittrader.common.domain.model.strategy.StrategyValues
import org.kentunc.bittrader.common.domain.model.strategy.StrategyValuesId
import org.kentunc.bittrader.common.domain.model.strategy.TradePosition
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.common.test.model.TestCandleList
import org.ta4j.core.BarSeries
import org.ta4j.core.BarSeriesManager
import org.ta4j.core.Strategy
import org.ta4j.core.TradingRecord
import org.ta4j.core.analysis.criteria.pnl.GrossProfitCriterion
import org.ta4j.core.num.Num
import java.util.stream.Stream

internal class AbstractStrategyServiceTest {

    @RelaxedMockK
    private lateinit var strategy: Strategy
    @RelaxedMockK
    private lateinit var strategyParamsRepository: StrategyParamsRepository<StrategyParams>

    private lateinit var target: AbstractStrategyService<StrategyParams>

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        target = object : AbstractStrategyService<StrategyParams>(strategyParamsRepository) {
            override suspend fun buildStrategy(series: BarSeries, params: StrategyParams): Strategy {
                return strategy
            }
        }
    }

    @ParameterizedTest
    @ArgumentsSource(MockStrategyProvider::class)
    fun testMakeOrderDecision(shouldEnter: Boolean, shouldExit: Boolean, expected: TradePosition) = runBlocking {
        // setup:
        val candleList = TestCandleList.create()
        val id = StrategyValuesId(ProductCode.BTC_JPY, Duration.DAYS)
        val params = mockk<StrategyParams>(relaxed = true)
        coEvery { strategyParamsRepository.get(id) } returns StrategyValues.of(id, params)

        every { strategy.shouldEnter(any()) } returns shouldEnter
        every { strategy.shouldExit(any()) } returns shouldExit

        // exercise:
        val actual = target.makeOrderDecision(candleList, id)

        // verify:
        assertEquals(expected, actual.position)
    }

    @Test
    fun testOptimize() = runBlocking {
        // setup:
        val candleList = TestCandleList.create()
        val id = StrategyValuesId(ProductCode.BTC_JPY, Duration.DAYS)
        val params = mockk<StrategyParams>()
        every { strategyParamsRepository.getForOptimize() } returns flowOf(params)

        val record = mockk<TradingRecord>()
        mockkConstructor(BarSeriesManager::class)
        every { anyConstructed<BarSeriesManager>().run(strategy) } returns record

        val profit = mockk<Num>(relaxed = true)
        mockkConstructor(GrossProfitCriterion::class)
        every { anyConstructed<GrossProfitCriterion>().calculate(any(), record) } returns profit

        // exercise:
        target.optimize(candleList, id)

        // verify:
        coVerify { strategyParamsRepository.save(id, params) }
    }

    private class MockStrategyProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                // shouldEnter, shouldExit, expected
                Arguments.arguments(true, false, TradePosition.SHOULD_BUY),
                Arguments.arguments(false, true, TradePosition.SHOULD_SELL),
                Arguments.arguments(false, false, TradePosition.NEUTRAL),
            )
        }
    }
}