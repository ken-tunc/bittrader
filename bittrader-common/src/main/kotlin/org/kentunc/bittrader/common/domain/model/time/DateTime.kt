package org.kentunc.bittrader.common.domain.model.time

import org.kentunc.bittrader.common.shared.annotation.Generated
import java.time.LocalDateTime

class DateTime private constructor(private val localDateTime: LocalDateTime) : Comparable<DateTime> {

    companion object {
        fun of(localDateTime: LocalDateTime) = DateTime(localDateTime)
    }

    fun toLocalDateTime() = localDateTime

    override fun toString() = localDateTime.toString()

    override fun compareTo(other: DateTime) = this.localDateTime.compareTo(other.localDateTime)
    @Generated
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DateTime

        if (localDateTime != other.localDateTime) return false

        return true
    }

    @Generated
    override fun hashCode(): Int {
        return localDateTime.hashCode()
    }
}
