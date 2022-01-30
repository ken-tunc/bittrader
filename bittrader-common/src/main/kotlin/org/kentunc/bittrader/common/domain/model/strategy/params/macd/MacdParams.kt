package org.kentunc.bittrader.common.domain.model.strategy.params.macd

import org.kentunc.bittrader.common.domain.model.strategy.params.StrategyParams
import org.kentunc.bittrader.common.domain.model.strategy.params.TimeFrame

data class MacdParams(val shortTimeFrame: TimeFrame, val longTimeFrame: TimeFrame, val signalTimeFrame: TimeFrame) :
    StrategyParams {

    init {
        require(shortTimeFrame < longTimeFrame) {
            "shortTimeFrame must be less than longTimeFrame. got short=$shortTimeFrame, long=$longTimeFrame"
        }
    }
}
