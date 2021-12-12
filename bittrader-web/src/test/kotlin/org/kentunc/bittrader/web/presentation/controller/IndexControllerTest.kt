package org.kentunc.bittrader.web.presentation.controller

import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.springframework.test.web.servlet.client.MockMvcWebTestClient
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view

internal class IndexControllerTest : AbstractControllerTest() {

    @Test
    fun testIndex() {
        val result = webTestClient.get()
            .uri("/")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .returnResult()
        MockMvcWebTestClient.resultActionsFor(result)
            .andExpect(view().name("index"))
            .andExpect(model().attribute("productCodes", ProductCode.values()))
    }
}
