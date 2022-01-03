package org.kentunc.bittrader.candle.api.infrastructure.configuration.strategy

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "bittrader.strategy.bollinger-bands")
data class BBandsConfigurationProperties(
    val defaultTimeFrame: Int,
    val defaultBuyK: Double,
    val defaultSellK: Double,
    private val timeFrameFrom: Int,
    private val timeFrameTo: Int,
    private val buyKFrom: Double,
    private val buyKTo: Double,
    private val sellKFrom: Double,
    private val sellKTo: Double,
    private val kStep: Double
) {
    val timeFrameRange
        get() = (timeFrameFrom..timeFrameTo)

    val buyKRange
        get() = generateSequence(buyKFrom) { it + kStep }
            .takeWhile { it <= buyKTo }

    val sellKRange
        get() = generateSequence(sellKFrom) { it + kStep }
            .takeWhile { it <= sellKTo }
}
