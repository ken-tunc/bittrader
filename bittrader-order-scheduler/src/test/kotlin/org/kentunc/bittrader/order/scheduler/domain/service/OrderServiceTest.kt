package org.kentunc.bittrader.order.scheduler.domain.service

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.OrderList
import org.kentunc.bittrader.common.domain.model.order.OrderSide
import org.kentunc.bittrader.order.scheduler.domain.repository.OrderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(OrderService::class)
internal class OrderServiceTest {

    @MockkBean(relaxed = true)
    private lateinit var orderRepository: OrderRepository

    @Autowired
    private lateinit var target: OrderService

    @Test
    fun testGetOrderList() = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val orderList = mockk<OrderList>()
        coEvery { orderRepository.getOrderList(productCode) } returns orderList

        // exercise:
        val actual = target.getOrderList(productCode)

        // verify:
        assertEquals(orderList, actual)
    }

    @Test
    fun testSendOrder() = runBlocking {
        val productCode = ProductCode.BTC_JPY
        val orderSide = OrderSide.BUY
        target.sendOrder(productCode, orderSide)
        coVerify { orderRepository.sendOrder(productCode, orderSide) }
    }
}
