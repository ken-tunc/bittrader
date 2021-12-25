package org.kentunc.bittrader.order.api.infrastructure.repository.live

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kentunc.bittrader.common.domain.model.market.Balance
import org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.BitflyerHttpPrivateApiClient
import org.kentunc.bittrader.order.api.domain.repository.BalanceRepository
import org.springframework.stereotype.Repository

@Repository
class LiveBalanceRepositoryImpl(private val bitflyerClient: BitflyerHttpPrivateApiClient) : BalanceRepository {

    override fun findAll(): Flow<Balance> {
        return bitflyerClient.getBalances()
            .map { it.toBalance() }
    }
}
