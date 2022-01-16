package org.kentunc.bittrader.common.domain.model.strategy.params.ema

import org.kentunc.bittrader.common.domain.model.strategy.params.StrategyParams
import org.kentunc.bittrader.common.domain.model.strategy.params.TimeFrame

data class EmaParams(val shortTimeFrame: TimeFrame, val longTimeFrame: TimeFrame) : StrategyParams {

    init {
        require(shortTimeFrame < longTimeFrame) {
            "short time frame must be less than long time frame. got short: $shortTimeFrame, long: $longTimeFrame"
        }
    }
}
