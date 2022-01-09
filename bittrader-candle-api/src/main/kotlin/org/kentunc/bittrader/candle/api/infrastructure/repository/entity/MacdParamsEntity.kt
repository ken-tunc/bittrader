package org.kentunc.bittrader.candle.api.infrastructure.repository.entity

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.strategy.StrategyValues
import org.kentunc.bittrader.common.domain.model.strategy.StrategyValuesId
import org.kentunc.bittrader.common.domain.model.strategy.params.MacdParams
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("macd_params")
data class MacdParamsEntity(
    @field:Id
    val productCode: ProductCode,
    val duration: Duration,
    val shortTimeFrame: Int,
    val longTimeFrame: Int,
    val signalTimeFrame: Int
) : StrategyParamsEntity {

    companion object {
        fun of(strategyValuesId: StrategyValuesId, macdParams: MacdParams) = MacdParamsEntity(
            productCode = strategyValuesId.productCode,
            duration = strategyValuesId.duration,
            shortTimeFrame = macdParams.shortTimeFrame,
            longTimeFrame = macdParams.longTimeFrame,
            signalTimeFrame = macdParams.signalTimeFrame
        )
    }

    fun toStrategyValues() = StrategyValues.of(
        id = StrategyValuesId(productCode, duration),
        params = MacdParams(shortTimeFrame, longTimeFrame, signalTimeFrame)
    )
}
