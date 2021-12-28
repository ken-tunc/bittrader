package org.kentunc.bittrader.candle.api.application.model

import org.kentunc.bittrader.common.domain.model.strategy.TradeDecision
import org.kentunc.bittrader.common.domain.model.strategy.TradePosition

class TotalOrderDecision private constructor(decisions: List<TradeDecision<*>>) {

    init {
        val strategyValuesIdSet = decisions.map { it.params.id }.toSet()
        require(strategyValuesIdSet.size <= 1) { "multiple strategy values ids are found. got=$strategyValuesIdSet" }
    }

    companion object {
        private val POSITION_PRIORITY =
            listOf(TradePosition.NEUTRAL, TradePosition.SHOULD_BUY, TradePosition.SHOULD_SELL)

        fun of(decisions: List<TradeDecision<*>>) = TotalOrderDecision(decisions)
    }

    val totalPosition: TradePosition = decisions.groupingBy { it.position }
        .eachCount()
        .toSortedMap(compareBy { POSITION_PRIORITY.indexOf(it) })
        .maxByOrNull { it.value }?.key
        ?: TradePosition.NEUTRAL
}
