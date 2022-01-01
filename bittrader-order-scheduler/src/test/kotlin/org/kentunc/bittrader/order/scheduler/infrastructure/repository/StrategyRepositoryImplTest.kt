package org.kentunc.bittrader.order.scheduler.infrastructure.repository

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.strategy.TradingPosition
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.common.infrastructure.webclient.http.candle.StrategyApiClient
import org.kentunc.bittrader.common.presentation.model.strategy.OptimizeRequest
import org.kentunc.bittrader.common.presentation.model.strategy.TradePositionRequest
import org.kentunc.bittrader.common.presentation.model.strategy.TradePositionResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(StrategyRepositoryImpl::class)
internal class StrategyRepositoryImplTest {

    @MockkBean(relaxed = true)
    private lateinit var strategyApiClient: StrategyApiClient

    @Autowired
    private lateinit var target: StrategyRepositoryImpl

    @Test
    fun testGetTradePosition() = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val duration = Duration.DAYS
        val position = TradingPosition.SHOULD_BUY
        coEvery {
            strategyApiClient.getPosition(
                TradePositionRequest(
                    productCode,
                    duration
                )
            )
        } returns TradePositionResponse(position)

        // exercise:
        val actual = target.getTradePosition(productCode, duration)

        // verify:
        assertEquals(position, actual)
    }

    @Test
    fun testOptimize() = runBlocking {
        val productCode = ProductCode.BTC_JPY
        val duration = Duration.DAYS
        target.optimize(productCode, duration)
        coVerify { strategyApiClient.optimize(OptimizeRequest(productCode, duration)) }
    }
}
