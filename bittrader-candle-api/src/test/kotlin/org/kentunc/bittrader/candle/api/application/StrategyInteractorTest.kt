package org.kentunc.bittrader.candle.api.application

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.candle.api.domain.model.strategy.OptimizeParamsSet
import org.kentunc.bittrader.candle.api.domain.model.strategy.StrategyValuesId
import org.kentunc.bittrader.candle.api.domain.model.strategy.TradingStrategy
import org.kentunc.bittrader.candle.api.domain.service.CandleService
import org.kentunc.bittrader.candle.api.domain.service.StrategyService
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.common.test.model.TestCandleList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(
    classes = [StrategyInteractor::class],
    initializers = [ConfigDataApplicationContextInitializer::class]
)
internal class StrategyInteractorTest {

    @MockkBean
    private lateinit var candleService: CandleService

    @MockkBean(relaxed = true)
    private lateinit var strategyService: StrategyService

    @Autowired
    private lateinit var target: StrategyInteractor

    @Test
    fun testGetStrategy() = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val duration = Duration.DAYS
        val candleList = TestCandleList.create()
        coEvery { candleService.findLatest(any()) } returns candleList
        val strategy = mockk<TradingStrategy>()
        coEvery {
            strategyService.getStrategy(
                candleList,
                StrategyValuesId(productCode, duration)
            )
        } returns strategy

        // exercise:
        val actual = target.getTradingStrategy(productCode, duration)

        // verify:
        assertEquals(strategy, actual)
        coVerify {
            candleService.findLatest(
                withArg {
                    assertAll(
                        { assertEquals(productCode, it.productCode) },
                        { assertEquals(duration, it.duration) },
                    )
                }
            )
        }
    }

    @Test
    fun testOptimizeStrategies_no_updates() = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val duration = Duration.DAYS
        val candleList = TestCandleList.create()
        val strategyValuesId = StrategyValuesId(productCode, duration)
        coEvery { candleService.findLatest(any()) } returns candleList

        coEvery { strategyService.optimize(candleList, strategyValuesId) } returns null

        // exercise:
        target.optimizeStrategies(productCode, duration)

        // verify
        coVerify {
            candleService.findLatest(
                withArg {
                    assertAll(
                        { assertEquals(productCode, it.productCode) },
                        { assertEquals(duration, it.duration) },
                    )
                }
            )
        }
        coVerify(exactly = 0) { strategyService.updateParams(any(), any()) }
    }

    @Test
    fun testOptimizeStrategies_updated() = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val duration = Duration.DAYS
        val candleList = TestCandleList.create()
        val strategyValuesId = StrategyValuesId(productCode, duration)
        coEvery { candleService.findLatest(any()) } returns candleList

        val optimizedParams = mockk<OptimizeParamsSet>()
        coEvery { strategyService.optimize(candleList, strategyValuesId) } returns optimizedParams

        // exercise:
        target.optimizeStrategies(productCode, duration)

        // verify
        coVerify {
            candleService.findLatest(
                withArg {
                    assertAll(
                        { assertEquals(productCode, it.productCode) },
                        { assertEquals(duration, it.duration) },
                    )
                }
            )
        }
        coVerify { strategyService.updateParams(strategyValuesId, optimizedParams) }
    }
}
