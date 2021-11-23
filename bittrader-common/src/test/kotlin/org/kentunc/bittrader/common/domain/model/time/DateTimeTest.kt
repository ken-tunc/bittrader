package org.kentunc.bittrader.common.domain.model.time

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.time.LocalDateTime
import java.util.stream.Stream

internal class DateTimeTest {

    @ParameterizedTest
    @ArgumentsSource(TestArgumentProvider::class)
    fun testEquals(localDateTime1: LocalDateTime, localDateTime2: LocalDateTime, expected: Boolean) {
        val dateTime1 = DateTime.of(localDateTime1)
        val dateTime2 = DateTime.of(localDateTime2)
        assertEquals(expected, dateTime1 == dateTime2)
    }

    private class TestArgumentProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
            Arguments.arguments(
                LocalDateTime.of(2020, 1, 1, 12, 30, 45),
                LocalDateTime.of(2020, 1, 1, 12, 30, 45),
                true
            ),
            Arguments.arguments(
                LocalDateTime.of(2020, 1, 1, 12, 30, 45),
                LocalDateTime.of(2020, 1, 1, 12, 30, 45, 1),
                false
            ),
            Arguments.arguments(
                LocalDateTime.of(2020, 1, 1, 12, 30, 44, 99),
                LocalDateTime.of(2020, 1, 1, 12, 30, 45),
                false
            ),
        )
    }
}
