package org.kentunc.bittrader.common.presentation.model.candle

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.server.ServerWebInputException
import java.util.stream.Stream

internal class CandleSearchRequestTest {

    @ParameterizedTest
    @ArgumentsSource(InvalidParamsProvider::class)
    fun testInvalidParams(productCode: String?, duration: String?, maxNum: String?) {
        val params = LinkedMultiValueMap<String, String>().apply {
            add("product_code", productCode)
            add("duration", duration)
            add("max_num", maxNum)
        }

        assertThrows<ServerWebInputException> { CandleSearchRequest.from(params) }
    }

    @Test
    fun testToMultiValueMap() {
        val request = CandleSearchRequest(ProductCode.BTC_JPY, Duration.DAYS, 100)
        val actual = request.toMultiValueMap()

        assertAll(
            { assertEquals(request.productCode.toString(), actual.getFirst("product_code")) },
            { assertEquals(request.duration.toString(), actual.getFirst("duration")) },
            { assertEquals(request.maxNum.toString(), actual.getFirst("max_num")) },
        )
    }

    private class InvalidParamsProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.arguments("invalid product_code", Duration.DAYS.toString(), Integer.valueOf(1).toString()),
                Arguments.arguments(null, Duration.DAYS.toString(), Integer.valueOf(1).toString()),
                Arguments.arguments(ProductCode.BTC_JPY.toString(), "invalid duration", Integer.valueOf(1).toString()),
                Arguments.arguments(ProductCode.BTC_JPY.toString(), null, Integer.valueOf(1).toString()),
                Arguments.arguments(ProductCode.BTC_JPY.toString(), Duration.DAYS.toString(), "not a number"),
            )
        }
    }
}
