package org.kentunc.bittrader.candle.api.infrastructure.repository.entity

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.strategy.params.StrategyValues
import org.kentunc.bittrader.common.domain.model.strategy.params.StrategyValuesId
import org.kentunc.bittrader.common.domain.model.strategy.params.TimeFrame
import org.kentunc.bittrader.common.domain.model.strategy.params.ema.EmaParams
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
            shortTimeFrame = emaParams.shortTimeFrame.toInt(),
            longTimeFrame = emaParams.longTimeFrame.toInt()
        )
    }

    fun toStrategyValues() = StrategyValues.of(
        id = StrategyValuesId(productCode, duration),
        params = EmaParams(
            shortTimeFrame = TimeFrame.of(shortTimeFrame),
            longTimeFrame = TimeFrame.of(longTimeFrame)
        )
    )
}
