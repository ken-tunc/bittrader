package org.kentunc.bittrader.common.domain.model.time

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.time.LocalDateTime
import java.util.stream.Stream

internal class TruncatedDateTimeTest {

    @ParameterizedTest
    @ArgumentsSource(TestArgumentProvider::class)
    fun testTruncate(original: LocalDateTime, duration: Duration, expected: LocalDateTime) {
        val dateTime = DateTime.of(original)
        val actual = TruncatedDateTime.of(dateTime, duration).toLocalDateTime()
        assertEquals(expected, actual)
    }

    private class TestArgumentProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
            // original, duration, expected
            arguments(
                LocalDateTime.of(2020, 1, 1, 12, 30, 45),
                Duration.MINUTES,
                LocalDateTime.of(2020, 1, 1, 12, 30, 0)
            ),
            arguments(
                LocalDateTime.of(2020, 1, 1, 12, 30, 45),
                Duration.HOURS,
                LocalDateTime.of(2020, 1, 1, 12, 0, 0)
            ),
            arguments(
                LocalDateTime.of(2020, 1, 1, 12, 30, 45),
                Duration.DAYS,
                LocalDateTime.of(2020, 1, 1, 0, 0, 0)
            )
        )
    }
}
