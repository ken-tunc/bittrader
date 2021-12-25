package org.kentunc.bittrader.web.application

import kotlinx.coroutines.flow.Flow
import org.kentunc.bittrader.common.domain.model.market.Balance
import org.kentunc.bittrader.web.domain.repository.BalanceRepository
import org.springframework.stereotype.Service

@Service
class BalanceService(private val balanceRepository: BalanceRepository) {

    fun get(): Flow<Balance> = balanceRepository.get()
}
