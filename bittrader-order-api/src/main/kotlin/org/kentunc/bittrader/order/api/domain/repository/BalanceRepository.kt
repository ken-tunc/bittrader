package org.kentunc.bittrader.order.api.domain.repository

import kotlinx.coroutines.flow.Flow
import org.kentunc.bittrader.common.domain.model.market.Balance

interface BalanceRepository {

    fun findAll(): Flow<Balance>
}
