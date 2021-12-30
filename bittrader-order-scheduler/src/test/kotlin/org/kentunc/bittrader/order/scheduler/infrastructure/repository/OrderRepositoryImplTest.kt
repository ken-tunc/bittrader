package org.kentunc.bittrader.order.scheduler.infrastructure.repository

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.OrderSide
import org.kentunc.bittrader.common.infrastructure.webclient.http.order.OrderApiClient
import org.kentunc.bittrader.common.presentation.model.order.OrderResponse
import org.kentunc.bittrader.common.presentation.model.order.OrderSignalRequest
import org.kentunc.bittrader.common.test.model.TestOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(OrderRepositoryImpl::class)
internal class OrderRepositoryImplTest {

    @MockkBean(relaxed = true)
    private lateinit var orderApiClient: OrderApiClient

    @Autowired
    private lateinit var target: OrderRepositoryImpl

    @Test
    fun testGetOrderList() = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val order = TestOrder.createOrder()
        val response = mockk<OrderResponse>()
        every { response.toOrder() } returns order
        every { orderApiClient.get(productCode) } returns flowOf(response)

        // exercise:
        val actual = target.getOrderList(productCode).toList()

        // verify:
        assertAll(
            { assertEquals(1, actual.size) },
            { assertEquals(order.detail.productCode, actual[0].detail.productCode) }
        )
    }

    @Test
    fun testSendOrder() = runBlocking {
        val productCode = ProductCode.BTC_JPY
        val orderSide = OrderSide.BUY
        target.sendOrder(productCode, orderSide)
        coVerify { orderApiClient.send(OrderSignalRequest(productCode, orderSide)) }
    }
}
