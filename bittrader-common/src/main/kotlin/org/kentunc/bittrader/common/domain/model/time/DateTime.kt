package org.kentunc.bittrader.common.domain.model.time

import java.time.LocalDateTime

class DateTime private constructor(private val localDateTime: LocalDateTime) {

    companion object {
        fun of(localDateTime: LocalDateTime) = DateTime(localDateTime)
    }

    fun toLocalDateTime() = localDateTime

    override fun toString() = localDateTime.toString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DateTime

        if (localDateTime != other.localDateTime) return false

        return true
    }

    override fun hashCode(): Int {
        return localDateTime.hashCode()
    }
}
