package org.kentunc.bittrader.common.infrastructure.webclient.http.order

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.presentation.model.market.CommissionRateResponse
import org.kentunc.bittrader.test.file.ResourceReader
import org.kentunc.bittrader.test.webclient.WebClientTest
import org.kentunc.bittrader.test.webclient.WebClientTestUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import java.math.BigDecimal

@WebClientTest(CommissionRateApiClient::class)
internal class CommissionRateApiClientTest {

    @Autowired
    private lateinit var util: WebClientTestUtil

    @Autowired
    private lateinit var target: CommissionRateApiClient

    @Test
    fun testGet() = runBlocking {
        // setup:
        val responseBody = ResourceReader.readResource("mock/order/get_commission_rates_product_code.json")
        util.enqueueResponse(body = responseBody)
        val expected = CommissionRateResponse(commissionRate = BigDecimal("0.0015"))

        // exercise:
        val actual = target.get(ProductCode.BTC_JPY)

        // verify:
        assertAll(
            { assertEquals(expected, actual) },
            { util.assertRequest(HttpMethod.GET, "/commission-rates/BTC_JPY") }
        )
    }
}
