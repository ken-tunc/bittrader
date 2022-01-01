package org.kentunc.bittrader.candle.api.infrastructure.repository.entity

import org.kentunc.bittrader.candle.api.domain.model.strategy.StrategyValues
import org.kentunc.bittrader.candle.api.domain.model.strategy.StrategyValuesId
import org.kentunc.bittrader.candle.api.domain.model.strategy.params.RsiParams
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("rsi_params")
data class RsiParamsEntity(
    @field:Id
    val productCode: ProductCode,
    val duration: Duration,
    val timeFrame: Int,
    val buyThreshold: Int,
    val sellThreshold: Int
) : StrategyParamsEntity {

    companion object {
        fun of(strategyValuesId: StrategyValuesId, rsiParams: RsiParams) = RsiParamsEntity(
            productCode = strategyValuesId.productCode,
            duration = strategyValuesId.duration,
            timeFrame = rsiParams.timeFrame,
            buyThreshold = rsiParams.buyThreshold,
            sellThreshold = rsiParams.sellThreshold
        )
    }

    fun toStrategyValues() = StrategyValues.of(
        id = StrategyValuesId(productCode, duration),
        params = RsiParams(timeFrame, buyThreshold, sellThreshold)
    )
}
