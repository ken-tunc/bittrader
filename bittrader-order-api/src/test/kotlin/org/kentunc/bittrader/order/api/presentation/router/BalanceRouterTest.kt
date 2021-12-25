package org.kentunc.bittrader.order.api.presentation.router

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.presentation.model.market.BalanceResponse
import org.kentunc.bittrader.common.test.model.TestBalance
import org.kentunc.bittrader.order.api.application.BalanceInteractor
import org.kentunc.bittrader.order.api.test.OrderApiTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBodyList

@OrderApiTest
@AutoConfigureWebTestClient
internal class BalanceRouterTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockkBean
    private lateinit var balanceInteractor: BalanceInteractor

    @Test
    fun testGet() {
        // setup:
        val balance = TestBalance.create()
        coEvery { balanceInteractor.getBalances() } returns flowOf(balance)

        // exercise & verify:
        webTestClient.get()
            .uri("/balances")
            .exchange()
            .expectAll(
                { it.expectStatus().isOk },
                { it.expectBodyList<BalanceResponse>().hasSize(1) }
            )
    }
}
