package org.kentunc.bittrader.common.domain.model.ticker

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.kentunc.bittrader.common.domain.model.quote.Price
import org.kentunc.bittrader.common.test.model.TestTicker
import java.util.stream.Stream

internal class TickerTest {

    @ParameterizedTest
    @ArgumentsSource(PriceProvider::class)
    fun testMidPrice(bestBid: Double, bestAsk: Double, midPrice: Double) {
        val ticker = TestTicker.create(bestBid = bestBid, bestAsk = bestAsk)

        assertEquals(Price.of(midPrice), ticker.midPrice)
    }

    private class PriceProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
            arguments(100.0, 100.0, 100.0),
            arguments(200.0, 100.0, 150.0),
            arguments(300.0, 500.0, 400.0)
        )
    }
}
