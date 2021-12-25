package org.kentunc.bittrader.order.api.infrastructure.repository.demo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.kentunc.bittrader.common.domain.model.market.Balance
import org.kentunc.bittrader.order.api.domain.repository.BalanceRepository

class DemoBalanceRepositoryImpl(private val demoBroker: DemoBroker) : BalanceRepository {

    override fun findAll(): Flow<Balance> {
        return demoBroker.getBalances()
            .asFlow()
    }
}
