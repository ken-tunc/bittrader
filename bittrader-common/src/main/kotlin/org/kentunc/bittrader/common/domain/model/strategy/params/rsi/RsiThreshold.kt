package org.kentunc.bittrader.common.domain.model.strategy.params.rsi

import org.kentunc.bittrader.common.shared.annotation.Generated

class RsiThreshold private constructor(private val threshold: Int) : Comparable<RsiThreshold> {

    init {
        require(threshold >= 0) { "RSI threshold must not be a negative number. got=$threshold" }
    }

    companion object {
        fun of(value: Int) = RsiThreshold(value)
    }

    override fun compareTo(other: RsiThreshold) = threshold.compareTo(other.threshold)

    fun toInt() = threshold

    @Generated
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RsiThreshold

        if (threshold != other.threshold) return false

        return true
    }

    @Generated
    override fun hashCode(): Int {
        return threshold
    }
}
