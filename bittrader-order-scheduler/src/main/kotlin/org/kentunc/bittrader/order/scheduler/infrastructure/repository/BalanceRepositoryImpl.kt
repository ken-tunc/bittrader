package org.kentunc.bittrader.order.scheduler.infrastructure.repository

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import org.kentunc.bittrader.common.domain.model.market.Balance
import org.kentunc.bittrader.common.domain.model.market.CurrencyCode
import org.kentunc.bittrader.common.infrastructure.webclient.http.order.BalanceApiClient
import org.kentunc.bittrader.order.scheduler.domain.repository.BalanceRepository
import org.springframework.stereotype.Repository

@Repository
class BalanceRepositoryImpl(private val balanceApiClient: BalanceApiClient) : BalanceRepository {

    override suspend fun get(currencyCode: CurrencyCode): Balance? {
        return balanceApiClient.get()
            .filter { it.currencyCode == currencyCode }
            .map { it.toBalance() }
            .firstOrNull()
    }
}
