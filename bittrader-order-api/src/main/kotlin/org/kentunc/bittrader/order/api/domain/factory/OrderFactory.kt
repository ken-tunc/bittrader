package org.kentunc.bittrader.order.api.domain.factory

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.OrderSignal
import org.kentunc.bittrader.order.api.domain.repository.BalanceRepository
import org.kentunc.bittrader.order.api.domain.repository.CommissionRateRepository
import org.kentunc.bittrader.order.api.domain.repository.TickerRepository
import org.springframework.stereotype.Component

@Component
class OrderFactory(
    private val balanceRepository: BalanceRepository,
    private val commissionRateRepository: CommissionRateRepository,
    private val tickerRepository: TickerRepository
) {

    suspend fun createBuyAllOrder(productCode: ProductCode): OrderSignal {
        val balance = balanceRepository.findAll()
            .filter { it.currencyCode == productCode.right }
            .first()
        val commissionRate = commissionRateRepository.get(productCode)
        val ticker = tickerRepository.get(productCode)
        return OrderSignal.ofBuyAll(productCode, balance, commissionRate, ticker)
    }

    suspend fun createSellAllOrder(productCode: ProductCode): OrderSignal {
        val balance = balanceRepository.findAll()
            .filter { it.currencyCode == productCode.left }
            .first()
        return OrderSignal.ofSellAll(productCode, balance)
    }
}
