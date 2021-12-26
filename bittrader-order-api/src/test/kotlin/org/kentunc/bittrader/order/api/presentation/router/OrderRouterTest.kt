package org.kentunc.bittrader.order.api.presentation.router

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.OrderList
import org.kentunc.bittrader.common.presentation.model.order.OrderResponse
import org.kentunc.bittrader.common.presentation.model.order.OrderSignalRequest
import org.kentunc.bittrader.common.test.model.TestOrder
import org.kentunc.bittrader.order.api.application.OrderInteractor
import org.kentunc.bittrader.order.api.test.OrderApiTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBodyList

@OrderApiTest
@AutoConfigureWebTestClient
internal class OrderRouterTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockkBean(relaxed = true)
    private lateinit var orderInteractor: OrderInteractor

    @Test
    fun testGet() {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val orderList = OrderList.of(listOf(TestOrder.createOrder()))
        coEvery { orderInteractor.getOrderListByProductCode(productCode) } returns orderList

        // exercise & verify:
        webTestClient.get()
            .uri {
                it.path("/orders/{productCode}")
                    .build(productCode)
            }
            .exchange()
            .expectAll(
                { it.expectStatus().isOk },
                { it.expectBodyList<OrderResponse>().hasSize(1) }
            )
    }

    @Test
    fun testSend() {
        // setup:
        val order = TestOrder.createOrderSignal()

        // exercise & verify:
        webTestClient.post()
            .uri("/orders")
            .bodyValue(OrderSignalRequest.of(order))
            .exchange()
            .expectAll(
                { it.expectStatus().isNoContent },
                { it.expectBody().isEmpty }
            )

        coVerify {
            orderInteractor.sendOrder(
                withArg {
                    assertEquals(order.detail.productCode, it.detail.productCode)
                }
            )
        }
    }
}
