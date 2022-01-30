package org.kentunc.bittrader.common.domain.model.strategy.params.rsi

import org.kentunc.bittrader.common.domain.model.strategy.params.StrategyParams
import org.kentunc.bittrader.common.domain.model.strategy.params.TimeFrame

data class RsiParams(val timeFrame: TimeFrame, val buyThreshold: RsiThreshold, val sellThreshold: RsiThreshold) :
    StrategyParams {

    companion object {
        private val MAX_BUY_THRESHOLD = RsiThreshold.of(30)
        private val MIN_SELL_THRESHOLD = RsiThreshold.of(70)
    }

    init {
        require(buyThreshold <= MAX_BUY_THRESHOLD) { "buyThreshold must be less than or equal to $MAX_BUY_THRESHOLD" }
        require(sellThreshold >= MIN_SELL_THRESHOLD) { "sellThreshold must be greater than or equal to $MIN_SELL_THRESHOLD" }
    }
}
