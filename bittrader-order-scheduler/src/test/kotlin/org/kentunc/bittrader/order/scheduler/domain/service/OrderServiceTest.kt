package org.kentunc.bittrader.order.scheduler.domain.service

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.kentunc.bittrader.common.domain.model.market.Balance
import org.kentunc.bittrader.common.domain.model.market.CommissionRate
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.MinutesToExpire
import org.kentunc.bittrader.common.domain.model.order.OrderList
import org.kentunc.bittrader.common.domain.model.order.OrderSignal
import org.kentunc.bittrader.common.domain.model.order.TimeInForce
import org.kentunc.bittrader.common.test.model.TestBalance
import org.kentunc.bittrader.order.scheduler.domain.repository.BalanceRepository
import org.kentunc.bittrader.order.scheduler.domain.repository.CommissionRateRepository
import org.kentunc.bittrader.order.scheduler.domain.repository.OrderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import java.util.stream.Stream

@SpringJUnitConfig(OrderService::class)
internal class OrderServiceTest {

    @MockkBean
    private lateinit var balanceRepository: BalanceRepository

    @MockkBean
    private lateinit var commissionRateRepository: CommissionRateRepository

    @MockkBean(relaxed = true)
    private lateinit var orderRepository: OrderRepository

    @Autowired
    private lateinit var target: OrderService

    @ParameterizedTest
    @ArgumentsSource(TestDataProvider::class)
    fun testSendBuyAllOrderIfPossible(canBuy: Boolean, balance: Balance?, numOfOrder: Int) = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val minutesToExpire = MinutesToExpire.of(5)
        val timeInForce = TimeInForce.IOC

        val orderList = mockk<OrderList>()
        every { orderList.canBuy() } returns canBuy
        coEvery { orderRepository.getOrderList(productCode) } returns orderList
        coEvery { balanceRepository.get(productCode.right) } returns balance

        val commissionRate = CommissionRate.of(0.0015)
        coEvery { commissionRateRepository.get(productCode) } returns commissionRate

        val orderSignal = mockk<OrderSignal>()
        mockkObject(OrderSignal)
        every {
            OrderSignal.ofBuyAll(
                productCode, balance ?: any(), commissionRate, minutesToExpire, timeInForce
            )
        } returns orderSignal

        // exercise:
        target.sendBuyAllOrderIfPossible(productCode, minutesToExpire, timeInForce)

        // verify:
        coVerify(exactly = numOfOrder) { orderRepository.sendOrder(orderSignal) }
    }

    @ParameterizedTest
    @ArgumentsSource(TestDataProvider::class)
    fun testSendSellAllOrderIfPossible(canSell: Boolean, balance: Balance?, numOfOrder: Int) = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val minutesToExpire = MinutesToExpire.of(5)
        val timeInForce = TimeInForce.IOC

        val orderList = mockk<OrderList>()
        every { orderList.canSell() } returns canSell
        coEvery { orderRepository.getOrderList(productCode) } returns orderList
        coEvery { balanceRepository.get(productCode.left) } returns balance

        val orderSignal = mockk<OrderSignal>()
        mockkObject(OrderSignal)
        every {
            OrderSignal.ofSellAll(
                productCode, balance ?: any(), minutesToExpire, timeInForce
            )
        } returns orderSignal

        // exercise:
        target.sendSellAllOrderIfPossible(productCode, minutesToExpire, timeInForce)

        // verify:
        coVerify(exactly = numOfOrder) { orderRepository.sendOrder(orderSignal) }
    }

    private class TestDataProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                // canBuy/canSell, balance, expectedNumOfOrder
                Arguments.arguments(false, TestBalance.create(), 0),
                Arguments.arguments(true, null, 0),
                Arguments.arguments(true, TestBalance.create(), 1)
            )
        }
    }
}
