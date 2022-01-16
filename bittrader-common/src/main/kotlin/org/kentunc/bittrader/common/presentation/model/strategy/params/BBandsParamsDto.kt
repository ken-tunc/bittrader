package org.kentunc.bittrader.common.presentation.model.strategy.params

import org.kentunc.bittrader.common.domain.model.strategy.params.TimeFrame
import org.kentunc.bittrader.common.domain.model.strategy.params.bbands.BBandsK
import org.kentunc.bittrader.common.domain.model.strategy.params.bbands.BBandsParams
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.Positive

data class BBandsParamsDto(
    @field:Positive
    val timeFrame: Int,
    @field:[Positive DecimalMax("3.0")]
    val buyK: Double,
    @field:[Positive DecimalMax("3.0")]
    val sellK: Double
) {

    companion object {
        fun of(bBandsParams: BBandsParams) = BBandsParamsDto(
            timeFrame = bBandsParams.timeFrame.toInt(),
            buyK = bBandsParams.buyK.toDouble(),
            sellK = bBandsParams.sellK.toDouble()
        )
    }

    fun toBBandsParams() = BBandsParams(
        timeFrame = TimeFrame.of(timeFrame),
        buyK = BBandsK.of(buyK),
        sellK = BBandsK.of(sellK)
    )
}
