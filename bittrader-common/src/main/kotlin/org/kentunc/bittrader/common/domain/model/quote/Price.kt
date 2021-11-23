package org.kentunc.bittrader.common.domain.model.quote

import java.math.BigDecimal

class Price private constructor(private val value: BigDecimal) : Comparable<Price> {

    companion object {
        fun of(value: BigDecimal) = Price(value)
        fun of(value: Long) = Price(BigDecimal.valueOf(value))
        fun of(value: Double) = Price(BigDecimal.valueOf(value))
    }

    fun toBigDecimal() = value

    override fun compareTo(other: Price): Int = this.value.compareTo(other.value)

    operator fun plus(other: Price) = Price(this.value.add(other.value))

    operator fun minus(other: Price) = Price(this.value.subtract(other.value))

    operator fun times(other: Price) = Price(this.value.multiply(other.value))

    operator fun div(other: Price) = Price(this.value.divide(other.value))

    override fun toString() = value.toString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Price

        // NOTE: BigDecimal#equals returns false for values with different numbers of digits
        return this.value.compareTo(other.value) == 0
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}
