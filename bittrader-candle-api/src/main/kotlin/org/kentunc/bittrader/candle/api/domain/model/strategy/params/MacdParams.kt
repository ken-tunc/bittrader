package org.kentunc.bittrader.candle.api.domain.model.strategy.params

import org.kentunc.bittrader.candle.api.domain.model.strategy.StrategyParams

data class MacdParams(val shortTimeFrame: Int, val longTimeFrame: Int, val signalTimeFrame: Int) : StrategyParams {

    init {
        require(shortTimeFrame > 0) { "shortTimeFrame must be a positive number. got=$shortTimeFrame" }
        require(longTimeFrame > 0) { "longTimeFrame must be a positive number. got=$shortTimeFrame" }
        require(signalTimeFrame > 0) { "signalTimeFrame must be a positive number. got=$shortTimeFrame" }
        require(shortTimeFrame < longTimeFrame) {
            "shortTimeFrame must be less than longTimeFrame. got short=$shortTimeFrame, long=$longTimeFrame"
        }
    }
}
