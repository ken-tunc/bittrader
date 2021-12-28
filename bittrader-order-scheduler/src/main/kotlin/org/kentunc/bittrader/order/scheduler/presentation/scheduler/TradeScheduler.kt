package org.kentunc.bittrader.order.scheduler.presentation.scheduler

import kotlinx.coroutines.runBlocking
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.order.scheduler.application.TradeInteractor
import org.springframework.scheduling.annotation.Scheduled

class TradeScheduler(
    private val productCode: ProductCode,
    private val duration: Duration,
    private val tradeInteractor: TradeInteractor
) {

    @Scheduled(cron = "\${bittrader.trade.cron}")
    fun scheduleTrade() = runBlocking {
        tradeInteractor.trade(productCode, duration)
    }
}
