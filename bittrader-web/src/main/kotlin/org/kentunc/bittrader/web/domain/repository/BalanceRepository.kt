package org.kentunc.bittrader.web.domain.repository

import kotlinx.coroutines.flow.Flow
import org.kentunc.bittrader.common.domain.model.market.Balance

interface BalanceRepository {

    fun get(): Flow<Balance>
}
