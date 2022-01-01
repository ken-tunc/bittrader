package org.kentunc.bittrader.candle.api.domain.model.candle

import org.kentunc.bittrader.common.domain.model.candle.Candle
import org.kentunc.bittrader.common.domain.model.candle.CandleList
import org.kentunc.bittrader.common.domain.model.time.DateTimeFactory
import org.ta4j.core.Bar
import org.ta4j.core.BarSeries
import org.ta4j.core.BaseBar
import org.ta4j.core.BaseBarSeries

private fun Candle.toBar(): Bar {
    return BaseBar(
        id.duration.toTemporalUnit().duration,
        DateTimeFactory.getZonedDateTime(id.dateTime.toLocalDateTime()),
        open.toBigDecimal().toDouble(),
        close.toBigDecimal().toDouble(),
        high.toBigDecimal().toDouble(),
        low.toBigDecimal().toDouble(),
        volume.toBigDecimal().toDouble()
    )
}

fun CandleList.toBarSeries(): BarSeries {
    val bars = this.toList()
        .map { it.toBar() }
    return BaseBarSeries(bars)
}
