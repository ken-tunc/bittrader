package org.kentunc.bittrader.common.domain.model.time

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

object DateTimeFactory {

    fun getByTimestamp(timestamp: String): DateTime {
        val localDateTime = LocalDateTime.ofInstant(Instant.parse(timestamp), getZoneId())
        return DateTime.of(localDateTime)
    }

    private fun getZoneId() = ZoneId.systemDefault()
}
