package org.kentunc.bittrader.common.presentation.model.strategy

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.server.ServerWebInputException

internal class TradePositionRequestTest {

    @ParameterizedTest
    @CsvSource("invalid_product_code,DAYS", ",DAYS", "BTC_JPY,invalid_duration", "BTC_JPY,")
    fun testInvalidParams(productCode: String?, duration: String?) {
        val params = LinkedMultiValueMap<String, String>().apply {
            add("product_code", productCode)
            add("duration", duration)
        }

        assertThrows<ServerWebInputException> { TradePositionRequest.from(params) }
    }

    @Test
    fun testToMultiValueMap() {
        val request = TradePositionRequest(ProductCode.BTC_JPY, Duration.DAYS)
        val actual = request.toMultiValueMap()

        assertAll(
            { assertEquals(request.productCode.toString(), actual.getFirst("product_code")) },
            { assertEquals(request.duration.toString(), actual.getFirst("duration")) }
        )
    }
}
