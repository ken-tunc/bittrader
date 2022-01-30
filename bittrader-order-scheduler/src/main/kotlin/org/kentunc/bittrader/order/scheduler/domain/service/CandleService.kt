package org.kentunc.bittrader.order.scheduler.domain.service

import org.kentunc.bittrader.order.scheduler.domain.repository.CandleRepository
import org.springframework.stereotype.Service

@Service
class CandleService(private val candleRepository: CandleRepository) : CandleRepository by candleRepository
