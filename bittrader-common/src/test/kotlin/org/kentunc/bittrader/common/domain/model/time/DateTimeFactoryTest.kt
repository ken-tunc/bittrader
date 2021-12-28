package org.kentunc.bittrader.common.domain.model.time

import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId
import java.time.ZonedDateTime

internal class DateTimeFactoryTest {

    private val baseDateTime = LocalDateTime.of(2019, Month.APRIL, 11, 5, 14, 12, 373991500)
    private val baseTimestamp = "2019-04-11T05:14:12.3739915Z"
    private val zoneId = ZoneId.of("UTC")

    @BeforeEach
    internal fun setUp() {
        mockkStatic(ZoneId::class)
        every { ZoneId.systemDefault() } returns zoneId
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now() } returns baseDateTime
    }

    @Test
    fun testGetByTimestamp() {
        val expected = DateTime.of(baseDateTime)
        val actual = DateTimeFactory.getByTimestamp(baseTimestamp)
        assertEquals(expected, actual)
    }

    @Test
    fun testGetInstant() {
        val expected = Instant.parse(baseTimestamp)
        val actual = DateTimeFactory.getInstant()
        assertEquals(expected, actual)
    }

    @Test
    fun testGetZonedDateTime() {
        val expected = ZonedDateTime.of(baseDateTime, zoneId)
        val actual = DateTimeFactory.getZonedDateTime(baseDateTime)
        assertEquals(expected, actual)
    }
}
