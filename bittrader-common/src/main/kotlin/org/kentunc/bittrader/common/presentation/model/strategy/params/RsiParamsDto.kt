package org.kentunc.bittrader.common.presentation.model.strategy.params

import org.kentunc.bittrader.common.domain.model.strategy.params.TimeFrame
import org.kentunc.bittrader.common.domain.model.strategy.params.rsi.RsiParams
import org.kentunc.bittrader.common.domain.model.strategy.params.rsi.RsiThreshold
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

data class RsiParamsDto(
    @field:Positive
    val timeFrame: Int,
    @field:PositiveOrZero
    val buyThreshold: Int,
    @field:PositiveOrZero
    val sellThreshold: Int
) {

    companion object {
        fun of(rsiParams: RsiParams) = RsiParamsDto(
            timeFrame = rsiParams.timeFrame.toInt(),
            buyThreshold = rsiParams.buyThreshold.toInt(),
            sellThreshold = rsiParams.sellThreshold.toInt()
        )
    }

    fun toRsiParams() = RsiParams(
        timeFrame = TimeFrame.of(timeFrame),
        buyThreshold = RsiThreshold.of(buyThreshold),
        sellThreshold = RsiThreshold.of(sellThreshold)
    )
}
