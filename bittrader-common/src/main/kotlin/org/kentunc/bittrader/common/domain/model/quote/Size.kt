package org.kentunc.bittrader.common.domain.model.quote

import org.kentunc.bittrader.common.shared.annotation.Generated
import java.math.BigDecimal

class Size private constructor(private val value: BigDecimal) {

    init {
        require(value >= BigDecimal.ZERO) { "Size must not be a negative number, got=$value" }
    }

    companion object {
        fun of(value: BigDecimal) = Size(value)
        fun of(value: Double) = Size(BigDecimal.valueOf(value))
    }

    fun toBigDecimal() = value

    operator fun plus(other: Size): Size {
        return Size(this.value.add(other.value))
    }

    operator fun minus(other: Size): Size {
        return Size(this.value.subtract(other.value))
    }

    @Generated
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Size

        // NOTE: BigDecimal#equals returns false for values with different numbers of digits
        return this.value.compareTo(other.value) == 0
    }

    @Generated
    override fun hashCode(): Int {
        return value.hashCode()
    }
}
