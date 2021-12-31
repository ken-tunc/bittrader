package org.kentunc.bittrader.web.presentation.model

import org.kentunc.bittrader.common.domain.model.candle.Candle
import java.math.BigDecimal
import java.time.LocalDateTime

data class VolumeBar(val dateTime: LocalDateTime, val volume: BigDecimal) {

    companion object {
        fun of(candle: Candle) = VolumeBar(candle.id.dateTime.toLocalDateTime(), candle.volume.toBigDecimal())
    }
}
