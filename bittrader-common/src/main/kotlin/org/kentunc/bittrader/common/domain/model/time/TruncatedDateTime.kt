package org.kentunc.bittrader.common.domain.model.time

import org.kentunc.bittrader.common.shared.annotation.Generated
import java.time.LocalDateTime

class TruncatedDateTime private constructor(private val localDateTime: LocalDateTime) {

    companion object {
        fun of(localDateTime: LocalDateTime, duration: Duration) =
            TruncatedDateTime(localDateTime.truncatedTo(duration.toTemporalUnit()))

        fun of(dateTime: DateTime, duration: Duration): TruncatedDateTime = of(dateTime.toLocalDateTime(), duration)
    }

    fun toLocalDateTime() = localDateTime

    override fun toString() = localDateTime.toString()

    @Generated
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TruncatedDateTime

        if (localDateTime != other.localDateTime) return false

        return true
    }

    @Generated
    override fun hashCode(): Int {
        return localDateTime.hashCode()
    }
}
