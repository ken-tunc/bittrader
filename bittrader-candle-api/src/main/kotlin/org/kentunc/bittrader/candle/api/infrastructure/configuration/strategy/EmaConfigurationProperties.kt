package org.kentunc.bittrader.candle.api.infrastructure.configuration.strategy

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "bittrader.strategy.ema")
data class EmaConfigurationProperties(
    val defaultShortTimeFrame: Int,
    val defaultLongTimeFrame: Int,
    private val shortTimeFrameFrom: Int,
    private val shortTimeFrameTo: Int,
    private val longTimeFrameFrom: Int,
    private val longTimeFrameTo: Int
) {
    val shortTimeFrameRange
        get() = (shortTimeFrameFrom..shortTimeFrameTo)

    val longTimeFrameRange
        get() = (longTimeFrameFrom..longTimeFrameTo)
}
