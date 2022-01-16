package org.kentunc.bittrader.common.domain.model.strategy.params

import org.kentunc.bittrader.common.domain.model.strategy.params.bbands.BBandsParams
import org.kentunc.bittrader.common.domain.model.strategy.params.rsi.RsiParams

data class OptimizeParamsSet(val rsiParams: RsiParams, val bBandsParams: BBandsParams) {

    companion object {
        fun product(rsiParamsList: List<RsiParams>, bBandsParamsList: List<BBandsParams>): List<OptimizeParamsSet> {
            return rsiParamsList.flatMap { macdParams ->
                bBandsParamsList.map { OptimizeParamsSet(macdParams, it) }
            }
        }
    }
}
