package org.kentunc.bittrader.candle.api.infrastructure.configuration.strategy

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "bittrader.strategy.rsi")
data class RsiConfigurationProperties(
    val defaultTimeFrame: Int,
    val defaultBuyThreshold: Int,
    val defaultSellThreshold: Int,
    private val timeFrameFrom: Int,
    private val timeFrameTo: Int,
    private val buyThresholdFrom: Int,
    private val buyThresholdTo: Int,
    private val sellThresholdFrom: Int,
    private val sellThresholdTo: Int,
    private val thresholdStep: Int
) {
    val timeFrameRange
        get() = IntRange(timeFrameFrom, timeFrameTo)

    val buyThresholdRange
        get() = IntRange(buyThresholdFrom, buyThresholdTo)
            .step(thresholdStep)

    val sellThresholdRange
        get() = IntRange(sellThresholdFrom, sellThresholdTo)
            .step(thresholdStep)
}
