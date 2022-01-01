package org.kentunc.bittrader.candle.api.domain.model.strategy

import org.kentunc.bittrader.candle.api.domain.model.strategy.params.MacdParams
import org.kentunc.bittrader.candle.api.domain.model.strategy.params.RsiParams

data class OptimizeParamsSet(val macdParams: MacdParams, val rsiParams: RsiParams) {

    companion object {
        fun product(macdParamsList: List<MacdParams>, rsiParamsList: List<RsiParams>): List<OptimizeParamsSet> {
            return macdParamsList.flatMap { macdParams ->
                rsiParamsList.map { OptimizeParamsSet(macdParams, it) }
            }
        }
    }
}
