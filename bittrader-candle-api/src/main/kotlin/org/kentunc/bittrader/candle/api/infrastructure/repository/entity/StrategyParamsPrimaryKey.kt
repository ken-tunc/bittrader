package org.kentunc.bittrader.candle.api.infrastructure.repository.entity

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.strategy.StrategyValuesId
import org.kentunc.bittrader.common.domain.model.time.Duration

data class StrategyParamsPrimaryKey(val productCode: ProductCode, val duration: Duration) {

    companion object {
        fun of(id: StrategyValuesId) = StrategyParamsPrimaryKey(id.productCode, id.duration)
    }
}
