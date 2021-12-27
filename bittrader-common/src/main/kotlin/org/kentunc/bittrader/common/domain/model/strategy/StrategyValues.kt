package org.kentunc.bittrader.common.domain.model.strategy

class StrategyValues<T : StrategyParams> private constructor(val id: StrategyValuesId, val values: T) {
    companion object {
        fun <T : StrategyParams> of(id: StrategyValuesId, values: T) = StrategyValues(id, values)
    }
}
