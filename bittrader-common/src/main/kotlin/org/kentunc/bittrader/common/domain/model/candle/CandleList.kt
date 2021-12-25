package org.kentunc.bittrader.common.domain.model.candle

class CandleList private constructor(candles: List<Candle>) {

    init {
        val productCodes = candles.map { it.id.productCode }.toSet()
        require(productCodes.size <= 1) { "multiple product codes are found. got=$productCodes" }

        val durations = candles.map { it.id.duration }.toSet()
        require(durations.size <= 1) { "multiple durations are found. got=$durations" }

        val duplicate = candles.groupingBy { it.id }
            .eachCount()
            .filter { it.value > 1 }
        require(duplicate.isEmpty()) { "duplicate candles are found. got=${duplicate.keys}" }
    }

    private val sorted = candles.sortedBy { it.id.dateTime }

    val size: Int
        get() = sorted.size

    val isEmpty: Boolean
        get() = sorted.isEmpty()

    companion object {
        fun of(candles: List<Candle>) = CandleList(candles)
    }

    fun latestOrNull(): Candle? = sorted.lastOrNull()

    fun toList() = sorted.toList()
}
