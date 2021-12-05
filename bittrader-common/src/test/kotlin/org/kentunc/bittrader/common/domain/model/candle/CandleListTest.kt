package org.kentunc.bittrader.common.domain.model.candle

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.common.test.model.TestCandle
import java.time.LocalDateTime
import java.util.stream.Stream

internal class CandleListTest {

    @Test
    fun testSort() {
        val baseDateTime = LocalDateTime.now()
        val oldestCandle = TestCandle.create(dateTime = baseDateTime, duration = Duration.MINUTES)
        val intermediateCandle = TestCandle.create(dateTime = baseDateTime.plusMinutes(10), duration = Duration.MINUTES)
        val latestCandle = TestCandle.create(dateTime = baseDateTime.plusMinutes(20), duration = Duration.MINUTES)

        val candleList = CandleList.of(listOf(latestCandle, oldestCandle, intermediateCandle))

        assertAll(
            { assertFalse(candleList.isEmpty) },
            { assertEquals(3, candleList.size) },
            { assertEquals(oldestCandle.id, candleList.toList()[0].id) },
            { assertEquals(intermediateCandle.id, candleList.toList()[1].id) },
            { assertEquals(latestCandle.id, candleList.toList()[2].id) },
            { assertEquals(latestCandle.id, candleList.latestOrNull()?.id) }
        )
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidCandlesProvider::class)
    fun testInvalidArgs(candles: List<Candle>) {
        assertThrows<IllegalArgumentException> {
            CandleList.of(candles)
        }
    }

    private class InvalidCandlesProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.arguments(
                    listOf(
                        TestCandle.create(productCode = ProductCode.BTC_JPY),
                        TestCandle.create(productCode = ProductCode.ETH_JPY)
                    )
                ),
                Arguments.arguments(
                    listOf(
                        TestCandle.create(duration = Duration.DAYS),
                        TestCandle.create(duration = Duration.HOURS)
                    )
                ),
                Arguments.arguments(
                    listOf(
                        TestCandle.create(),
                        TestCandle.create()
                    )
                )
            )
        }
    }
}
