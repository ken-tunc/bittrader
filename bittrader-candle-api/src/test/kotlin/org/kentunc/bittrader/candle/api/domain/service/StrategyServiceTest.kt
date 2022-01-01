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
import org.kentunc.bittrader.candle.api.domain.model.strategy.OptimizeParamsSet
import org.kentunc.bittrader.candle.api.domain.model.strategy.StrategyValues
import org.kentunc.bittrader.candle.api.domain.model.strategy.StrategyValuesId
import org.kentunc.bittrader.candle.api.domain.model.strategy.TradingStrategy
import org.kentunc.bittrader.candle.api.domain.model.strategy.params.MacdParams
import org.kentunc.bittrader.candle.api.domain.model.strategy.params.RsiParams
import org.kentunc.bittrader.candle.api.domain.repository.MacdParamsRepository
import org.kentunc.bittrader.candle.api.domain.repository.RsiParamsRepository
import org.kentunc.bittrader.common.domain.model.candle.CandleList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.ta4j.core.num.Num

@SpringJUnitConfig(StrategyService::class)
internal class StrategyServiceTest {

    @MockkBean(relaxed = true)
    private lateinit var macdParamsRepository: MacdParamsRepository

    @MockkBean(relaxed = true)
    private lateinit var rsiParamsRepository: RsiParamsRepository

    @Autowired
    private lateinit var target: StrategyService

    @Test
    fun testGetStrategy() = runBlocking {
        // setup:
        val candleList = mockk<CandleList>()
        val strategyValuesId = mockk<StrategyValuesId>()
        val macdParams = mockk<MacdParams>(relaxed = true)
        coEvery { macdParamsRepository.get(strategyValuesId) } returns StrategyValues.of(strategyValuesId, macdParams)
        val rsiParams = mockk<RsiParams>(relaxed = true)
        coEvery { rsiParamsRepository.get(strategyValuesId) } returns StrategyValues.of(strategyValuesId, rsiParams)

        val strategy = mockk<TradingStrategy>()
        mockkObject(TradingStrategy)
        every { TradingStrategy.of(candleList, macdParams, rsiParams) } returns strategy

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
        val rsiParamsForOptimize = mockk<RsiParams>()
        coEvery { macdParamsRepository.getForOptimize() } returns flowOf(macdParamsForOptimize)
        coEvery { rsiParamsRepository.getForOptimize() } returns flowOf(rsiParamsForOptimize)

        val optimizeParamsSet = mockk<OptimizeParamsSet>()
        every { optimizeParamsSet.macdParams } returns macdParamsForOptimize
        every { optimizeParamsSet.rsiParams } returns rsiParamsForOptimize

        mockkObject(OptimizeParamsSet)
        coEvery { OptimizeParamsSet.product(any(), any()) } returns listOf(optimizeParamsSet)

        val tradingStrategy = mockk<TradingStrategy>()
        val profit = mockk<Num>(relaxed = true)
        every { tradingStrategy.getProfit() } returns profit
        mockkObject(TradingStrategy)
        every { TradingStrategy.of(candleList, macdParamsForOptimize, rsiParamsForOptimize) } returns tradingStrategy

        val macdParams = mockk<MacdParams>()
        coEvery { macdParamsRepository.get(strategyValuesId) } returns StrategyValues.of(strategyValuesId, macdParams)
        val rsiParams = mockk<RsiParams>()
        coEvery { rsiParamsRepository.get(strategyValuesId) } returns StrategyValues.of(strategyValuesId, rsiParams)

        // exercise:
        target.optimize(candleList, strategyValuesId)

        // verify:
        coVerify { macdParamsRepository.save(strategyValuesId, macdParamsForOptimize) }
        coVerify { rsiParamsRepository.save(strategyValuesId, rsiParamsForOptimize) }
    }
}
