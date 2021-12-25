package org.kentunc.bittrader.order.api.infrastructure.repository.live

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
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
        val orderSignal = TestOrder.createOrderSignal()
        coEvery { bitflyerClient.getOrders(productCode) } returns flowOf(
            OrderResponse(
                productCode = orderSignal.detail.productCode,
                side = orderSignal.detail.orderSide,
                orderType = orderSignal.detail.orderType,
                price = orderSignal.detail.price!!.toBigDecimal(),
                size = orderSignal.detail.size.toBigDecimal(),
                averagePrice = orderSignal.averagePrice.toBigDecimal(),
                state = orderSignal.state,
                orderDate = orderSignal.orderDate.toLocalDateTime()
            )
        )

        // exercise:
        val actual = target.find(productCode).toList()

        // verify:
        assertEquals(orderSignal.detail.productCode, actual[0].detail.productCode)
    }

    @Test
    fun testSend() = runBlocking {
        // setup:
        val order = TestOrder.createOrder()

        // exercise:
        target.send(order)

        // verify:
        coVerify { bitflyerClient.sendOrder(withArg {
            assertEquals(order.detail.productCode, it.productCode)
        }) }
    }
}
