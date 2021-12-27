package org.kentunc.bittrader.candle.api.domain.service

import org.kentunc.bittrader.common.domain.model.candle.CandleList
import org.kentunc.bittrader.common.domain.model.strategy.StrategyParams
import org.kentunc.bittrader.common.domain.model.strategy.StrategyValuesId
import org.kentunc.bittrader.common.domain.model.strategy.TradeDecision

interface StrategyService<T : StrategyParams> {

    suspend fun makeOrderDecision(candleList: CandleList, id: StrategyValuesId): TradeDecision<T>

    suspend fun optimize(candleList: CandleList, id: StrategyValuesId)
}
