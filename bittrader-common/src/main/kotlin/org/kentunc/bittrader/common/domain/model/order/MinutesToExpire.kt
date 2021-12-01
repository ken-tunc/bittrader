package org.kentunc.bittrader.common.domain.model.order

class MinutesToExpire private constructor(private val value: Int) {

    init {
        require(value >= 0) { "MinutesToExpire must be 0 or a positive number." }
    }

    companion object {
        fun of(value: Int) = MinutesToExpire(value)
    }

    fun toInt() = value

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MinutesToExpire

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value
    }
}
