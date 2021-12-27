package org.kentunc.bittrader.common.domain.model.time

import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit

enum class Duration(private val unit: TemporalUnit) {

    MINUTES(ChronoUnit.MINUTES),
    HOURS(ChronoUnit.HOURS),
    DAYS(ChronoUnit.DAYS);

    fun toTemporalUnit(): TemporalUnit = unit
}
