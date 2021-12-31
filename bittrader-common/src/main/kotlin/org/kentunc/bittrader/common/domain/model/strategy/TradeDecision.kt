package org.kentunc.bittrader.common.domain.model.strategy

data class TradeDecision<T : StrategyParams>(val params: StrategyValues<T>, val position: TradingPosition)
