package org.kentunc.bittrader.web.presentation.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.OrderList
import org.kentunc.bittrader.common.test.model.TestOrder
import org.kentunc.bittrader.web.application.OrderService
import org.springframework.test.web.servlet.client.MockMvcWebTestClient
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

internal class OrderControllerTest : AbstractControllerTest() {

    @MockkBean
    private lateinit var orderService: OrderService

    @Test
    fun testOrders() {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val orderList = OrderList.of(listOf(TestOrder.createOrder()))
        coEvery { orderService.find(productCode) } returns orderList

        // exercise & verify:
        val result = webTestClient.get()
            .uri {
                it.path("/orders/{productCode}")
                    .build(productCode)
            }
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .returnResult()

        MockMvcWebTestClient.resultActionsFor(result)
            .andExpect(MockMvcResultMatchers.view().name("orders"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("orders"))
    }
}
