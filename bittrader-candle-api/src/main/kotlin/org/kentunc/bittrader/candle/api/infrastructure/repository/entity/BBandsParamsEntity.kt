package org.kentunc.bittrader.candle.api.infrastructure.repository.entity

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.strategy.params.StrategyValues
import org.kentunc.bittrader.common.domain.model.strategy.params.StrategyValuesId
import org.kentunc.bittrader.common.domain.model.strategy.params.TimeFrame
import org.kentunc.bittrader.common.domain.model.strategy.params.bbands.BBandsK
import org.kentunc.bittrader.common.domain.model.strategy.params.bbands.BBandsParams
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("bbands_params")
data class BBandsParamsEntity(
    @field:Id
    val productCode: ProductCode,
    val duration: Duration,
    val timeFrame: Int,
    val buyK: Double,
    val sellK: Double
) : StrategyParamsEntity {

    companion object {
        fun of(strategyValuesId: StrategyValuesId, bBandsParams: BBandsParams) = BBandsParamsEntity(
            productCode = strategyValuesId.productCode,
            duration = strategyValuesId.duration,
            timeFrame = bBandsParams.timeFrame.toInt(),
            buyK = bBandsParams.buyK.toDouble(),
            sellK = bBandsParams.sellK.toDouble()
        )
    }

    fun toStrategyValues() = StrategyValues.of(
        id = StrategyValuesId(productCode, duration),
        params = BBandsParams(
            timeFrame = TimeFrame.of(timeFrame),
            buyK = BBandsK.of(buyK),
            sellK = BBandsK.of(sellK)
        )
    )
}
