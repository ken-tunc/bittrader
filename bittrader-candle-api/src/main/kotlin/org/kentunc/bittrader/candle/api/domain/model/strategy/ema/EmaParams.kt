package org.kentunc.bittrader.candle.api.domain.model.strategy.ema

import org.kentunc.bittrader.candle.api.domain.model.strategy.StrategyParams

data class EmaParams(val shortTimeFrame: Int, val longTimeFrame: Int) : StrategyParams {

    init {
        require(shortTimeFrame > 0) { "short time frame must be a positive integer. got=$shortTimeFrame" }
        require(longTimeFrame > 0) { "long time frame must be a positive integer. got=$longTimeFrame" }
        require(shortTimeFrame < longTimeFrame) {
            "short time frame must be less than long time frame. got short: $shortTimeFrame, long: $longTimeFrame"
        }
    }
}
