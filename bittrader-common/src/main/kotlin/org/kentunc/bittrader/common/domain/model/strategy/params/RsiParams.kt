package org.kentunc.bittrader.common.domain.model.strategy.params

import org.kentunc.bittrader.common.domain.model.strategy.StrategyParams

data class RsiParams(val timeFrame: Int, val buyThreshold: Int, val sellThreshold: Int) : StrategyParams {

    companion object {
        private const val MAX_BUY_THRESHOLD = 30
        private const val MIN_SELL_THRESHOLD = 70
    }

    init {
        require(timeFrame > 0) { "timeFrame must be a positive number. got=$timeFrame" }
        require(buyThreshold <= MAX_BUY_THRESHOLD) { "buyThreshold must be less than or equal to $MAX_BUY_THRESHOLD" }
        require(sellThreshold >= MIN_SELL_THRESHOLD) { "sellThreshold must be greater than or equal to $MIN_SELL_THRESHOLD" }
    }
}
