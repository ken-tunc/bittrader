package org.kentunc.bittrader.common.domain.model.time

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

object DateTimeFactory {

    fun getByTimestamp(timestamp: String): DateTime {
        val localDateTime = LocalDateTime.ofInstant(Instant.parse(timestamp), getZoneId())
        return DateTime.of(localDateTime)
    }

    fun getInstant(): Instant {
        return getZonedDateTime(LocalDateTime.now()).toInstant()
    }

    fun getZonedDateTime(localDateTime: LocalDateTime): ZonedDateTime {
        return ZonedDateTime.of(localDateTime, getZoneId())
    }

    private fun getZoneId() = ZoneId.systemDefault()
}
