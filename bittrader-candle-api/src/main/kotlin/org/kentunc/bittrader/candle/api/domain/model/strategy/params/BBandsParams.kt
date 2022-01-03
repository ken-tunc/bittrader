package org.kentunc.bittrader.candle.api.domain.model.strategy.params

import org.kentunc.bittrader.candle.api.domain.model.strategy.StrategyParams

data class BBandsParams(val timeFrame: Int, val buyK: Double, val sellK: Double) : StrategyParams {

    init {
        require(timeFrame > 0) { "timeFrame must be a positive number. got=$timeFrame" }
        require(0 < buyK && buyK <= 3.0) { "buyK must be a value s.t. 0 < k < 3.0 got=$buyK" }
        require(0 < sellK && sellK <= 3.0) { "sellK must be a value s.t. 0 < k < 3.0 got=$sellK" }
        require(buyK > sellK) { "buyK must be greater than sellK. buyK=$buyK, sellK=$sellK" }
    }
}
