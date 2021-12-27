package org.kentunc.bittrader.candle.api.infrastructure.repository.entity

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.strategy.EmaParams
import org.kentunc.bittrader.common.domain.model.strategy.StrategyValues
import org.kentunc.bittrader.common.domain.model.strategy.StrategyValuesId
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("ema_params")
data class EmaParamsEntity(
    @field:Id
    val productCode: ProductCode,
    val duration: Duration,
    val shortTimeFrame: Int,
    val longTimeFrame: Int
) : StrategyParamsEntity {

    companion object {
        fun of(strategyValuesId: StrategyValuesId, emaParams: EmaParams) = EmaParamsEntity(
            productCode = strategyValuesId.productCode,
            duration = strategyValuesId.duration,
            shortTimeFrame = emaParams.shortTimeFrame,
            longTimeFrame = emaParams.longTimeFrame
        )
    }

    fun toStrategyValues() = StrategyValues.of(
        id = StrategyValuesId(productCode, duration),
        values = EmaParams(shortTimeFrame, longTimeFrame)
    )
}
