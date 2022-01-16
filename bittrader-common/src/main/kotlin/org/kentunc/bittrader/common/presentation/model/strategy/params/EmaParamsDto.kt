package org.kentunc.bittrader.common.presentation.model.strategy.params

import org.kentunc.bittrader.common.domain.model.strategy.params.TimeFrame
import org.kentunc.bittrader.common.domain.model.strategy.params.ema.EmaParams
import javax.validation.constraints.Positive

data class EmaParamsDto(
    @field:Positive
    val shortTimeFrame: Int,
    @field:Positive
    val longTimeFrame: Int
) {

    companion object {
        fun of(emaParams: EmaParams) = EmaParamsDto(
            shortTimeFrame = emaParams.shortTimeFrame.toInt(),
            longTimeFrame = emaParams.longTimeFrame.toInt()
        )
    }

    fun toEmaParams() = EmaParams(
        shortTimeFrame = TimeFrame.of(shortTimeFrame),
        longTimeFrame = TimeFrame.of(longTimeFrame)
    )
}
