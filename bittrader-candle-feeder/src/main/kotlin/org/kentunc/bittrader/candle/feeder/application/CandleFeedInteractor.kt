package org.kentunc.bittrader.candle.feeder.application

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.kentunc.bittrader.candle.feeder.domain.service.CandleService
import org.kentunc.bittrader.candle.feeder.domain.service.TickerService
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.springframework.stereotype.Service

@Service
class CandleFeedInteractor(private val tickerService: TickerService, private val candleService: CandleService) {

    fun feedCandles() {
        ProductCode.values().toList().parallelStream()
            .forEach { productCode ->
                runBlocking {
                    tickerService.subscribe(productCode)
                        .collect { candleService.feed(it) }
                }
            }
    }
}
