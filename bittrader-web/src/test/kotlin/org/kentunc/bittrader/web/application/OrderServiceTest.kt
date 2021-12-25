package org.kentunc.bittrader.web.application

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.OrderList
import org.kentunc.bittrader.common.test.model.TestOrder
import org.kentunc.bittrader.web.domain.repository.OrderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(OrderService::class)
internal class OrderServiceTest {

    @MockkBean
    private lateinit var orderRepository: OrderRepository

    @Autowired
    private lateinit var target: OrderService

    @Test
    fun testFind() = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val orderList = OrderList.of(listOf(TestOrder.createOrder()))
        coEvery { orderRepository.find(productCode) } returns orderList

        // exercise:
        val actual = target.find(productCode)

        // verify:
        assertEquals(orderList, actual)
    }
}
