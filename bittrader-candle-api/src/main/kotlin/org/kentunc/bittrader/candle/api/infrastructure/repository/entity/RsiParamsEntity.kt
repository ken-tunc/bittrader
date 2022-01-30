package org.kentunc.bittrader.candle.api.infrastructure.repository.entity

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.strategy.params.StrategyValues
import org.kentunc.bittrader.common.domain.model.strategy.params.StrategyValuesId
import org.kentunc.bittrader.common.domain.model.strategy.params.TimeFrame
import org.kentunc.bittrader.common.domain.model.strategy.params.rsi.RsiParams
import org.kentunc.bittrader.common.domain.model.strategy.params.rsi.RsiThreshold
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
            timeFrame = rsiParams.timeFrame.toInt(),
            buyThreshold = rsiParams.buyThreshold.toInt(),
            sellThreshold = rsiParams.sellThreshold.toInt()
        )
    }

    fun toStrategyValues() = StrategyValues.of(
        id = StrategyValuesId(productCode, duration),
        params = RsiParams(
            timeFrame = TimeFrame.of(timeFrame),
            buyThreshold = RsiThreshold.of(buyThreshold),
            sellThreshold = RsiThreshold.of(sellThreshold)
        )
    )
}
