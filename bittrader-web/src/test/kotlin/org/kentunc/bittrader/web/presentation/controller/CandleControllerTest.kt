package org.kentunc.bittrader.web.presentation.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.common.test.model.TestCandleList
import org.kentunc.bittrader.web.application.CandleService
import org.springframework.test.web.servlet.client.MockMvcWebTestClient
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view

internal class CandleControllerTest : AbstractControllerTest() {

    @MockkBean
    private lateinit var candleService: CandleService

    @Test
    fun testCandlestickChart_default() {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val duration = Duration.MINUTES
        val candleList = TestCandleList.create()
        coEvery { candleService.search(any()) } returns candleList

        // exercise & verify:
        val result = webTestClient.get()
            .uri {
                it.path("/candles/{productCode}")
                    .build(productCode)
            }
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .returnResult()
        MockMvcWebTestClient.resultActionsFor(result)
            .andExpect(view().name("candle"))
            .andExpect(model().attribute("productCodes", ProductCode.values()))
            .andExpect(model().attribute("durations", Duration.values()))
            .andExpect(model().attribute("activeDuration", duration))
            .andExpect(model().attributeExists("candleSticks"))

        coVerify {
            candleService.search(withArg {
                assertEquals(productCode, it.productCode)
                assertEquals(duration, it.duration)
            })
        }
    }

    @ParameterizedTest
    @EnumSource(Duration::class)
    fun testCandlestickChart_specified(duration: Duration) {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val candleList = TestCandleList.create()
        coEvery { candleService.search(any()) } returns candleList

        // exercise & verify:
        val result = webTestClient.get()
            .uri {
                it.path("/candles/{productCode}")
                    .queryParam("duration", duration)
                    .build(productCode)
            }
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .returnResult()
        MockMvcWebTestClient.resultActionsFor(result)
            .andExpect(view().name("candle"))
            .andExpect(model().attribute("productCodes", ProductCode.values()))
            .andExpect(model().attribute("durations", Duration.values()))
            .andExpect(model().attribute("activeDuration", duration))
            .andExpect(model().attributeExists("candleSticks"))

        coVerify {
            candleService.search(withArg {
                assertEquals(productCode, it.productCode)
                assertEquals(duration, it.duration)
            })
        }
    }
}
