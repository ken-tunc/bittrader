package org.kentunc.bittrader.order.api.domain.service

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.test.model.TestOrder
import org.kentunc.bittrader.order.api.domain.repository.OrderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(OrderService::class)
internal class OrderServiceTest {

    @MockkBean(relaxed = true)
    private lateinit var orderRepository: OrderRepository

    @Autowired
    private lateinit var target: OrderService

    @Test
    fun testGetOrderSignalList() = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val orderSignals = flowOf(TestOrder.createOrder())
        coEvery { orderRepository.find(productCode) } returns orderSignals

        // exercise:
        val actual = target.getOrderSignalList(productCode)

        // verify:
        assertEquals(orderSignals.toList(), actual.toList())
    }

    @Test
    fun testSend() = runBlocking {
        // setup:
        val order = TestOrder.createOrderSignal()

        // exercise:
        target.send(order)

        // verify:
        coVerify { orderRepository.send(order) }
    }
}
