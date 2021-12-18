package org.kentunc.bittrader.order.api.domain.service

import kotlinx.coroutines.flow.Flow
import org.kentunc.bittrader.common.domain.model.market.Balance
import org.kentunc.bittrader.order.api.domain.repository.BalanceRepository
import org.springframework.stereotype.Service

@Service
class BalanceService(private val balanceRepository: BalanceRepository) {

    fun getAll(): Flow<Balance> = balanceRepository.findAll()
}
