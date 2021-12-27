package org.kentunc.bittrader.candle.api.application.model

import org.kentunc.bittrader.common.domain.model.strategy.TradeDecision
import org.kentunc.bittrader.common.domain.model.strategy.TradePosition

class TotalOrderDecision private constructor(decisions: List<TradeDecision<*>>) {

    init {
        val strategyValuesIdSet = decisions.map { it.params.id }.toSet()
        require(strategyValuesIdSet.size <= 1) { "multiple strategy values ids are found. got=$strategyValuesIdSet" }
    }

    companion object {
        fun of(decisions: List<TradeDecision<*>>) = TotalOrderDecision(decisions)
    }

    val totalPosition: TradePosition = decisions.groupingBy { it.position }
        .eachCount()
        .maxByOrNull { it.value }?.key
        ?: TradePosition.NEUTRAL
}
