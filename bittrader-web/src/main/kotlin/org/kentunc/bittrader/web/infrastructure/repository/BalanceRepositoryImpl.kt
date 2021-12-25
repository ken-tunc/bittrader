package org.kentunc.bittrader.web.infrastructure.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kentunc.bittrader.common.domain.model.market.Balance
import org.kentunc.bittrader.common.infrastructure.webclient.http.order.BalanceApiClient
import org.kentunc.bittrader.web.domain.repository.BalanceRepository
import org.springframework.stereotype.Repository

@Repository
class BalanceRepositoryImpl(private val balanceApiClient: BalanceApiClient) : BalanceRepository {

    override fun get(): Flow<Balance> {
        return balanceApiClient.get()
            .map { it.toBalance() }
    }
}
