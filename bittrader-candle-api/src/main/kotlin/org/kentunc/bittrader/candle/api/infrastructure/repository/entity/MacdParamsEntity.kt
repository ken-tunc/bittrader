package org.kentunc.bittrader.candle.api.infrastructure.repository.entity

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.strategy.params.StrategyValues
import org.kentunc.bittrader.common.domain.model.strategy.params.StrategyValuesId
import org.kentunc.bittrader.common.domain.model.strategy.params.TimeFrame
import org.kentunc.bittrader.common.domain.model.strategy.params.macd.MacdParams
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
            shortTimeFrame = macdParams.shortTimeFrame.toInt(),
            longTimeFrame = macdParams.longTimeFrame.toInt(),
            signalTimeFrame = macdParams.signalTimeFrame.toInt()
        )
    }

    fun toStrategyValues() = StrategyValues.of(
        id = StrategyValuesId(productCode, duration),
        params = MacdParams(
            shortTimeFrame = TimeFrame.of(shortTimeFrame),
            longTimeFrame = TimeFrame.of(longTimeFrame),
            signalTimeFrame = TimeFrame.of(signalTimeFrame)
        )
    )
}
