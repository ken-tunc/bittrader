package org.kentunc.bittrader.order.scheduler.domain.service

import org.kentunc.bittrader.order.scheduler.domain.repository.StrategyRepository
import org.springframework.stereotype.Service

@Service
class StrategyService(private val strategyRepository: StrategyRepository) : StrategyRepository by strategyRepository
