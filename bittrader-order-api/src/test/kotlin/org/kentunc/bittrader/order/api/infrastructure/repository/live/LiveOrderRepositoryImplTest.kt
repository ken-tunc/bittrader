package org.kentunc.bittrader.order.api.infrastructure.repository.live

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.BitflyerHttpPrivateApiClient
import org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.model.order.OrderResponse
import org.kentunc.bittrader.common.test.model.TestOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(LiveOrderRepositoryImpl::class)
internal class LiveOrderRepositoryImplTest {

    @MockkBean(relaxed = true)
    private lateinit var bitflyerClient: BitflyerHttpPrivateApiClient

    @Autowired
    private lateinit var target: LiveOrderRepositoryImpl

    @Test
    fun testFind() = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val order = TestOrder.createOrder()
        val response = mockk<OrderResponse>()
        every { response.toOrder() } returns order
        every { bitflyerClient.getOrders(productCode) } returns flowOf(response)

        // exercise:
        val actual = target.find(productCode).toList()

        // verify:
        assertAll(
            { assertEquals(1, actual.size) },
            { assertEquals(order, actual[0]) }
        )
    }

    @Test
    fun testSend() = runBlocking {
        // setup:
        val order = TestOrder.createOrderSignal()

        // exercise:
        target.send(order)

        // verify:
        coVerify {
            bitflyerClient.sendOrder(
                withArg {
                    assertEquals(order.detail.productCode, it.productCode)
                }
            )
        }
    }
}
