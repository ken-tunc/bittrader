package org.kentunc.bittrader.common.domain.model.market

import org.kentunc.bittrader.common.domain.model.quote.Size
import java.math.BigDecimal

class CommissionRate private constructor(private val value: BigDecimal) {

    init {
        require(value >= BigDecimal.ZERO) { "CommissionRate must not be a negative number, got=$value" }
    }

    companion object {
        fun of(value: BigDecimal) = CommissionRate(value)
    }

    fun fee(size: Size): Size {
        return Size.of(size.toBigDecimal() * value)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CommissionRate

        // NOTE: BigDecimal#equals returns false for values with different numbers of digits
        return this.value.compareTo(other.value) == 0
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}
