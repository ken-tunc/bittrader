package org.kentunc.bittrader.candle.feeder.application

import kotlinx.coroutines.flow.collect
import org.kentunc.bittrader.candle.feeder.domain.service.CandleService
import org.kentunc.bittrader.candle.feeder.domain.service.TickerService
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.springframework.stereotype.Service

@Service
class CandleFeedInteractor(private val tickerService: TickerService, private val candleService: CandleService) {

    suspend fun feedCandles(productCodes: Collection<ProductCode>) {
        tickerService.subscribe(productCodes)
            .collect { candleService.feed(it) }
    }
}
