package org.kentunc.bittrader.candle.api.application.model

import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.strategy.StrategyValues
import org.kentunc.bittrader.common.domain.model.strategy.StrategyValuesId
import org.kentunc.bittrader.common.domain.model.strategy.TradeDecision
import org.kentunc.bittrader.common.domain.model.strategy.TradePosition
import org.kentunc.bittrader.common.domain.model.time.Duration
import java.util.stream.Stream

internal class TotalOrderDecisionTest {

    @Test
    fun testInstantiate_invalid() {
        val decisions = listOf(
            TradeDecision(
                StrategyValues.of(
                    StrategyValuesId(ProductCode.BTC_JPY, Duration.DAYS),
                    mockk()
                ),
                TradePosition.SHOULD_BUY
            ),
            TradeDecision(
                StrategyValues.of(
                    StrategyValuesId(ProductCode.BTC_JPY, Duration.MINUTES),
                    mockk()
                ),
                TradePosition.SHOULD_BUY
            )
        )
        assertThrows<IllegalArgumentException> { TotalOrderDecision.of(decisions) }
    }

    @ParameterizedTest
    @ArgumentsSource(TradePositionProvider::class)
    fun testTotalPosition(positions: List<TradePosition>, expected: TradePosition) {
        // setup:
        val decisions = positions.map {
            TradeDecision(
                StrategyValues.of(
                    StrategyValuesId(ProductCode.BTC_JPY, Duration.DAYS),
                    mockk()
                ),
                it
            )
        }

        // exercise:
        val actual = TotalOrderDecision.of(decisions).totalPosition

        // verify:
        assertEquals(expected, actual)
    }

    private class TradePositionProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.arguments(listOf<TradePosition>(), TradePosition.NEUTRAL),
                Arguments.arguments(
                    listOf(TradePosition.SHOULD_SELL),
                    TradePosition.SHOULD_SELL
                ),
                Arguments.arguments(
                    listOf(TradePosition.SHOULD_SELL, TradePosition.NEUTRAL),
                    TradePosition.NEUTRAL
                ),
                Arguments.arguments(
                    listOf(TradePosition.SHOULD_BUY, TradePosition.NEUTRAL, TradePosition.SHOULD_BUY),
                    TradePosition.SHOULD_BUY
                ),
                Arguments.arguments(
                    listOf(TradePosition.SHOULD_BUY, TradePosition.NEUTRAL, TradePosition.SHOULD_SELL),
                    TradePosition.NEUTRAL
                )
            )
        }
    }
}
