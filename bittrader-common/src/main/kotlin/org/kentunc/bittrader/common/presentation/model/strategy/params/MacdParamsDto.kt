package org.kentunc.bittrader.common.presentation.model.strategy.params

import org.kentunc.bittrader.common.domain.model.strategy.params.TimeFrame
import org.kentunc.bittrader.common.domain.model.strategy.params.macd.MacdParams
import javax.validation.constraints.Positive

data class MacdParamsDto(
    @field:Positive
    val shortTimeFrame: Int,
    @field:Positive
    val longTimeFrame: Int,
    @field:Positive
    val signalTimeFrame: Int
) {

    companion object {
        fun of(macdParams: MacdParams) = MacdParamsDto(
            shortTimeFrame = macdParams.shortTimeFrame.toInt(),
            longTimeFrame = macdParams.longTimeFrame.toInt(),
            signalTimeFrame = macdParams.signalTimeFrame.toInt()
        )
    }

    fun toMacdParams() = MacdParams(
        shortTimeFrame = TimeFrame.of(shortTimeFrame),
        longTimeFrame = TimeFrame.of(longTimeFrame),
        signalTimeFrame = TimeFrame.of(signalTimeFrame)
    )
}
