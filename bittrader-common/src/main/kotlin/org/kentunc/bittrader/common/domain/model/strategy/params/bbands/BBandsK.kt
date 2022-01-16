package org.kentunc.bittrader.common.domain.model.strategy.params.bbands

import org.kentunc.bittrader.common.shared.annotation.Generated

class BBandsK private constructor(private val k: Double) : Comparable<BBandsK> {

    init {
        require(0 < k && k <= 3.0) { "BBands' k must be a value s.t. 0 < k <= 3.0 got=$k" }
    }

    companion object {
        fun of(value: Double) = BBandsK(value)
    }

    fun toDouble() = k

    override fun compareTo(other: BBandsK) = k.compareTo(other.k)

    @Generated
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BBandsK

        if (k != other.k) return false

        return true
    }

    @Generated
    override fun hashCode(): Int {
        return k.hashCode()
    }
}
