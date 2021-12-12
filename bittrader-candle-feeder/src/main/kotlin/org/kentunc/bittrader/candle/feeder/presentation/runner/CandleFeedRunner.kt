package org.kentunc.bittrader.candle.feeder.presentation.runner

import kotlinx.coroutines.runBlocking
import org.kentunc.bittrader.candle.feeder.application.CandleFeedInteractor
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("feed")
class CandleFeedRunner(private val candleFeedInteractor: CandleFeedInteractor) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        runBlocking {
            candleFeedInteractor.feedCandles(ProductCode.values().toSet())
        }
    }
}
