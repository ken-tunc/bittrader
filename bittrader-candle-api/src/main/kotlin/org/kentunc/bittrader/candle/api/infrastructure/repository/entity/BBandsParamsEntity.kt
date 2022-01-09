package org.kentunc.bittrader.candle.api.infrastructure.repository.entity

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.strategy.StrategyValues
import org.kentunc.bittrader.common.domain.model.strategy.StrategyValuesId
import org.kentunc.bittrader.common.domain.model.strategy.params.BBandsParams
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
            timeFrame = bBandsParams.timeFrame,
            buyK = bBandsParams.buyK,
            sellK = bBandsParams.sellK
        )
    }

    fun toStrategyValues() = StrategyValues.of(
        id = StrategyValuesId(productCode, duration),
        params = BBandsParams(timeFrame, buyK, sellK)
    )
}
