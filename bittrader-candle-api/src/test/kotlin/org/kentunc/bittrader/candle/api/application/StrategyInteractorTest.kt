package org.kentunc.bittrader.candle.api.application

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.candle.api.application.model.TotalOrderDecision
import org.kentunc.bittrader.candle.api.domain.service.CandleService
import org.kentunc.bittrader.candle.api.domain.service.EmaStrategyService
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.strategy.EmaParams
import org.kentunc.bittrader.common.domain.model.strategy.TradeDecision
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
    private lateinit var emaStrategyService: EmaStrategyService

    @Autowired
    private lateinit var target: StrategyInteractor

    @Test
    fun testMakeTradingDecision() = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val duration = Duration.DAYS
        val candleList = TestCandleList.create()
        coEvery { candleService.findLatest(any()) } returns candleList
        val decision = mockk<TradeDecision<EmaParams>>()
        coEvery { emaStrategyService.makeOrderDecision(candleList, any()) } returns decision

        val decisions = mockk<TotalOrderDecision>()
        mockkObject(TotalOrderDecision)
        every { TotalOrderDecision.of(listOf(decision)) } returns decisions

        // exercise:
        val actual = target.makeTradingDecision(productCode, duration)

        // verify:
        assertEquals(decisions, actual)
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
        coVerify {
            emaStrategyService.makeOrderDecision(
                any(),
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
    fun testOptimizeStrategies() = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val duration = Duration.DAYS
        val candleList = TestCandleList.create()
        coEvery { candleService.findLatest(any()) } returns candleList

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
        coVerify {
            emaStrategyService.optimize(
                candleList,
                withArg {
                    assertAll(
                        { assertEquals(productCode, it.productCode) },
                        { assertEquals(duration, it.duration) },
                    )
                }
            )
        }
    }
}
