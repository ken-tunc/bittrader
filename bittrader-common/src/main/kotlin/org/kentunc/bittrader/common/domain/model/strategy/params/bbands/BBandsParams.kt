package org.kentunc.bittrader.common.domain.model.strategy.params.bbands

import org.kentunc.bittrader.common.domain.model.strategy.params.StrategyParams
import org.kentunc.bittrader.common.domain.model.strategy.params.TimeFrame

data class BBandsParams(val timeFrame: TimeFrame, val buyK: BBandsK, val sellK: BBandsK) : StrategyParams {

    init {
        require(buyK > sellK) { "buyK must be greater than sellK. buyK=$buyK, sellK=$sellK" }
    }
}
