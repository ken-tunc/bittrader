package org.kentunc.bittrader.common.domain.model.quote

import java.math.BigDecimal

class Volume private constructor(private val value: BigDecimal) {

    init {
        require(value >= BigDecimal.ZERO) { "Volume must not be a negative number, got=$value" }
    }

    companion object {
        fun of(value: Double) = Volume(BigDecimal.valueOf(value))
    }

    fun toBigDecimal() = value

    operator fun plus(other: Volume) = Volume(this.value.add(other.value))

    override fun toString() = value.toString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Volume

        // NOTE: BigDecimal#equals returns false for values with different numbers of digits
        return this.value.compareTo(other.value) == 0
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}
