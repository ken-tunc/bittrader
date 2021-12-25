package org.kentunc.bittrader.order.api.application

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.OrderSignalList
import org.kentunc.bittrader.common.test.model.TestOrder
import org.kentunc.bittrader.order.api.domain.service.OrderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(OrderInteractor::class)
internal class OrderInteractorTest {

    @MockkBean(relaxed = true)
    private lateinit var orderService: OrderService

    @Autowired
    private lateinit var target: OrderInteractor

    @Test
    fun testGetOrderSignalListByProductCode() = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val orderSignalList = OrderSignalList.of(listOf())
        coEvery { orderService.getOrderSignalList(productCode) } returns orderSignalList

        // exercise:
        val actual = target.getOrderSignalListByProductCode(productCode)

        // verify:
        assertEquals(orderSignalList, actual)
    }

    @Test
    fun testSendOrder() = runBlocking {
        // setup:
        val order = TestOrder.createOrder()

        // exercise:
        target.sendOrder(order)

        // verify:
        coVerify { orderService.send(order) }
    }
}
