package org.kentunc.bittrader.order.scheduler.application

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.OrderList
import org.kentunc.bittrader.common.domain.model.order.OrderSide
import org.kentunc.bittrader.common.domain.model.strategy.TradePosition
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.order.scheduler.domain.service.OrderService
import org.kentunc.bittrader.order.scheduler.domain.service.StrategyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(TradeInteractor::class)
internal class TradeInteractorTest {

    @MockkBean(relaxed = true)
    private lateinit var strategyService: StrategyService

    @MockkBean(relaxed = true)
    private lateinit var orderService: OrderService

    @Autowired
    private lateinit var target: TradeInteractor

    @ParameterizedTest
    @CsvSource(
        "SHOULD_BUY,true,true,1,0",
        "SHOULD_BUY,false,false,0,0",
        "SHOULD_SELL,true,true,0,1",
        "SHOULD_SELL,false,false,0,0",
        "NEUTRAL,true,true,0,0"
    )
    fun testTrade(position: TradePosition, canBuy: Boolean, canSell: Boolean, numBuyOrder: Int, numSellOrder: Int) = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val duration = Duration.DAYS
        coEvery { strategyService.getTradePosition(productCode, duration) } returns position

        val orderList = mockk<OrderList>()
        every { orderList.canBuy() } returns canBuy
        every { orderList.canSell() } returns canSell
        coEvery { orderService.getOrderList(productCode) } returns orderList

        // exercise:
        target.trade(productCode, duration)

        // verify:
        coVerify(exactly = numBuyOrder) { orderService.sendOrder(productCode, OrderSide.BUY) }
        coVerify(exactly = numSellOrder) { orderService.sendOrder(productCode, OrderSide.SELL) }
    }

    @Test
    fun testOptimizeStrategies() = runBlocking {
        val productCode = ProductCode.BTC_JPY
        val duration = Duration.DAYS
        target.optimizeStrategies(productCode, duration)
        coVerify { strategyService.optimize(productCode, duration) }
    }
}
