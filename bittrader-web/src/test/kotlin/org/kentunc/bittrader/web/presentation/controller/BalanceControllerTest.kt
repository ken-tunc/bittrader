package org.kentunc.bittrader.web.presentation.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.test.model.TestBalance
import org.kentunc.bittrader.web.application.BalanceService
import org.springframework.test.web.reactive.server.returnResult
import org.springframework.test.web.servlet.client.MockMvcWebTestClient
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view

internal class BalanceControllerTest : AbstractControllerTest() {

    @MockkBean
    private lateinit var balanceService: BalanceService

    @Test
    fun testBalances() {
        // setup:
        val balance = TestBalance.create()
        every { balanceService.get() } returns flowOf(balance)

        // exercise & verify:
        val result = webTestClient.get()
            .uri("/balances")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .returnResult()

        MockMvcWebTestClient.resultActionsFor(result)
            .andExpect(view().name("balances"))
            .andExpect(model().attributeExists("balances"))
    }
}
