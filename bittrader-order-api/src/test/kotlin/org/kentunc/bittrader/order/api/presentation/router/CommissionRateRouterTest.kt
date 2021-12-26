package org.kentunc.bittrader.order.api.presentation.router

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.market.CommissionRate
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.presentation.model.market.CommissionRateResponse
import org.kentunc.bittrader.order.api.application.CommissionRateInteractor
import org.kentunc.bittrader.order.api.test.OrderApiTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@OrderApiTest
@AutoConfigureWebTestClient
internal class CommissionRateRouterTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockkBean
    private lateinit var commissionRateInteractor: CommissionRateInteractor

    @Test
    fun testGet() {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val commissionRate = CommissionRate.of(100.0)
        coEvery { commissionRateInteractor.getByProductCode(productCode) } returns commissionRate

        // exercise & verify:
        webTestClient.get()
            .uri {
                it.path("/commission-rates/{productCode}")
                    .build(productCode)
            }
            .exchange()
            .expectAll(
                { it.expectStatus().isOk },
                { spec ->
                    spec.expectBody<CommissionRateResponse>()
                        .consumeWith { assertEquals(commissionRate.toBigDecimal(), it.responseBody?.commissionRate) }
                }
            )
    }
}
