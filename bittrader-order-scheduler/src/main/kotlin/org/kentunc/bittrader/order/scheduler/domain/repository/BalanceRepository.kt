package org.kentunc.bittrader.order.scheduler.domain.repository

import org.kentunc.bittrader.common.domain.model.market.Balance
import org.kentunc.bittrader.common.domain.model.market.CurrencyCode

interface BalanceRepository {

    suspend fun get(currencyCode: CurrencyCode): Balance?
}
