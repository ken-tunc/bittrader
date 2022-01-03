package org.kentunc.bittrader.candle.api.domain.model.strategy

import org.kentunc.bittrader.candle.api.domain.model.strategy.params.BBandsParams
import org.kentunc.bittrader.candle.api.domain.model.strategy.params.MacdParams

data class OptimizeParamsSet(val macdParams: MacdParams, val bBandsParams: BBandsParams) {

    companion object {
        fun product(macdParamsList: List<MacdParams>, bBandsParamsList: List<BBandsParams>): List<OptimizeParamsSet> {
            return macdParamsList.flatMap { macdParams ->
                bBandsParamsList.map { OptimizeParamsSet(macdParams, it) }
            }
        }
    }
}
