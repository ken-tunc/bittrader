package org.kentunc.bittrader.candle.api.infrastructure.configuration.strategy

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "bittrader.strategy.macd")
data class MacdConfigurationProperties(
    val defaultShortTimeFrame: Int,
    val defaultLongTimeFrame: Int,
    val defaultSignalTimeFrame: Int,
    private val shortTimeFrameFrom: Int,
    private val shortTimeFrameTo: Int,
    private val longTimeFrameFrom: Int,
    private val longTimeFrameTo: Int,
    private val signalTimeFrameFrom: Int,
    private val signalTimeFrameTo: Int
) {
    val shortTimeFrameRange
        get() = (shortTimeFrameFrom..shortTimeFrameTo)

    val longTimeFrameRange
        get() = (longTimeFrameFrom..longTimeFrameTo)

    val signalTimeFrameRange
        get() = (signalTimeFrameFrom..signalTimeFrameTo)
}
