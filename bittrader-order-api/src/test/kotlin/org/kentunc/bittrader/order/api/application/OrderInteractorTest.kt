package org.kentunc.bittrader.order.api.application

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.OrderList
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
        val orderList = OrderList.of(listOf())
        coEvery { orderService.getOrderSignalList(productCode) } returns orderList

        // exercise:
        val actual = target.getOrderListByProductCode(productCode)

        // verify:
        assertEquals(orderList, actual)
    }

    @Test
    fun testSendBuyAllOrder() = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY

        // exercise:
        target.sendBuyAllOrder(productCode)

        // verify:
        coVerify { orderService.sendBuyAllOrder(productCode) }
    }

    @Test
    fun testSendSellAllOrder() = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY

        // exercise:
        target.sendSellAllOrder(productCode)

        // verify:
        coVerify { orderService.sendSellAllOrder(productCode) }
    }
}
