package org.kentunc.bittrader.candle.api.domain.model.strategy

class StrategyValues<T : StrategyParams> private constructor(val id: StrategyValuesId, val params: T) {
    companion object {
        fun <T : StrategyParams> of(id: StrategyValuesId, params: T) = StrategyValues(id, params)
    }
}
