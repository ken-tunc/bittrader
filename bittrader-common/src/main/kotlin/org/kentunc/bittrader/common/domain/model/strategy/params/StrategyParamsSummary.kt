package org.kentunc.bittrader.common.domain.model.strategy.params

import org.kentunc.bittrader.common.domain.model.strategy.params.bbands.BBandsParams
import org.kentunc.bittrader.common.domain.model.strategy.params.ema.EmaParams
import org.kentunc.bittrader.common.domain.model.strategy.params.macd.MacdParams
import org.kentunc.bittrader.common.domain.model.strategy.params.rsi.RsiParams

class StrategyParamsSummary(
    val id: StrategyValuesId,
    val bBandsParams: BBandsParams,
    val emaParams: EmaParams,
    val macdParams: MacdParams,
    val rsiParams: RsiParams
)
