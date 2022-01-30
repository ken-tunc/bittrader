package org.kentunc.bittrader.common.domain.model.strategy.params

import org.kentunc.bittrader.common.shared.annotation.Generated

class TimeFrame private constructor(private val timeFrame: Int) : Comparable<TimeFrame> {

    init {
        require(timeFrame > 0) { "timeFrame must be a positive number. got=$timeFrame" }
    }

    companion object {
        fun of(value: Int) = TimeFrame(value)
    }

    fun toInt() = timeFrame

    override fun compareTo(other: TimeFrame) = timeFrame.compareTo(other.timeFrame)

    @Generated
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TimeFrame

        if (timeFrame != other.timeFrame) return false

        return true
    }

    @Generated
    override fun hashCode(): Int {
        return timeFrame
    }
}
