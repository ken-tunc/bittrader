package org.kentunc.bittrader.candle.api.domain.service

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.kentunc.bittrader.candle.api.domain.model.strategy.OptimizeParamsSet
import org.kentunc.bittrader.candle.api.domain.model.strategy.StrategyValues
import org.kentunc.bittrader.candle.api.domain.model.strategy.StrategyValuesId
import org.kentunc.bittrader.candle.api.domain.model.strategy.TradingStrategy
import org.kentunc.bittrader.candle.api.domain.model.strategy.params.BBandsParams
import org.kentunc.bittrader.candle.api.domain.model.strategy.params.MacdParams
import org.kentunc.bittrader.candle.api.domain.repository.BBandsParamsRepository
import org.kentunc.bittrader.candle.api.domain.repository.MacdParamsRepository
import org.kentunc.bittrader.common.domain.model.candle.CandleList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.ta4j.core.num.Num

@SpringJUnitConfig(StrategyService::class)
internal class StrategyServiceTest {

    @MockkBean(relaxed = true)
    private lateinit var macdParamsRepository: MacdParamsRepository

    @MockkBean(relaxed = true)
    private lateinit var bBandsParamsRepository: BBandsParamsRepository

    @Autowired
    private lateinit var target: StrategyService

    @Test
    fun testGetStrategy() = runBlocking {
        // setup:
        val candleList = mockk<CandleList>()
        val strategyValuesId = mockk<StrategyValuesId>()
        val macdParams = mockk<MacdParams>(relaxed = true)
        coEvery { macdParamsRepository.get(strategyValuesId) } returns StrategyValues.of(strategyValuesId, macdParams)
        val bBandsParams = mockk<BBandsParams>(relaxed = true)
        coEvery { bBandsParamsRepository.get(strategyValuesId) } returns
            StrategyValues.of(strategyValuesId, bBandsParams)

        val strategy = mockk<TradingStrategy>()
        mockkObject(TradingStrategy)
        every { TradingStrategy.of(candleList, macdParams, bBandsParams) } returns strategy

        // exercise:
        val actual = target.getStrategy(candleList, strategyValuesId)

        // verify:
        assertEquals(strategy, actual)
    }

    @Test
    fun testOptimize() = runBlocking {
        // setup:
        val candleList = mockk<CandleList>()
        val strategyValuesId = mockk<StrategyValuesId>()

        val macdParamsForOptimize = mockk<MacdParams>()
        val bBandsParamsForOptimize = mockk<BBandsParams>()
        coEvery { macdParamsRepository.getForOptimize() } returns flowOf(macdParamsForOptimize)
        coEvery { bBandsParamsRepository.getForOptimize() } returns flowOf(bBandsParamsForOptimize)

        val optimizeParamsSet = mockk<OptimizeParamsSet>()
        every { optimizeParamsSet.macdParams } returns macdParamsForOptimize
        every { optimizeParamsSet.bBandsParams } returns bBandsParamsForOptimize

        mockkObject(OptimizeParamsSet)
        coEvery { OptimizeParamsSet.product(any(), any()) } returns listOf(optimizeParamsSet)

        val tradingStrategy = mockk<TradingStrategy>()
        val profit = mockk<Num>(relaxed = true)
        every { tradingStrategy.getCriterionValue() } returns profit
        mockkObject(TradingStrategy)
        every { TradingStrategy.of(candleList, macdParamsForOptimize, bBandsParamsForOptimize) } returns tradingStrategy

        // exercise:
        val actual = target.optimize(candleList, strategyValuesId)

        // verify:
        assertAll(
            { assertEquals(macdParamsForOptimize, actual?.macdParams) },
            { assertEquals(bBandsParamsForOptimize, actual?.bBandsParams) },
        )
    }

    @Test
    fun testUpdateParams_no_updates() = runBlocking {
        // setup:
        val strategyValuesId = mockk<StrategyValuesId>()
        val macdParams = mockk<MacdParams>()
        val bBandsParams = mockk<BBandsParams>()
        val params = OptimizeParamsSet(macdParams, bBandsParams)

        coEvery { macdParamsRepository.get(strategyValuesId) } returns
            StrategyValues.of(strategyValuesId, macdParams)
        coEvery { bBandsParamsRepository.get(strategyValuesId) } returns
            StrategyValues.of(strategyValuesId, bBandsParams)

        // exercise:
        target.updateParams(strategyValuesId, params)

        // verify:
        coVerify(exactly = 0) { macdParamsRepository.save(any(), any()) }
        coVerify(exactly = 0) { bBandsParamsRepository.save(any(), any()) }
    }

    @Test
    fun testUpdateParams_updated() = runBlocking {
        // setup:
        val strategyValuesId = mockk<StrategyValuesId>()
        val macdParams = mockk<MacdParams>()
        val bBandsParams = mockk<BBandsParams>()
        val params = OptimizeParamsSet(macdParams, bBandsParams)

        coEvery { macdParamsRepository.get(strategyValuesId) } returns mockk(relaxed = true)
        coEvery { bBandsParamsRepository.get(strategyValuesId) } returns mockk(relaxed = true)

        // exercise:
        target.updateParams(strategyValuesId, params)

        // verify:
        coVerify(exactly = 1) { macdParamsRepository.save(strategyValuesId, macdParams) }
        coVerify(exactly = 1) { bBandsParamsRepository.save(strategyValuesId, bBandsParams) }
    }
}
