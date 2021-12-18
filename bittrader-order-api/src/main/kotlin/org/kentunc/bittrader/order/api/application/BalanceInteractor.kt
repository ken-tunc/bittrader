package org.kentunc.bittrader.order.api.application

import kotlinx.coroutines.flow.Flow
import org.kentunc.bittrader.common.domain.model.market.Balance
import org.kentunc.bittrader.order.api.domain.service.BalanceService
import org.springframework.stereotype.Service

@Service
class BalanceInteractor(private val balanceService: BalanceService) {

    fun getBalances(): Flow<Balance> = balanceService.getAll()
}
