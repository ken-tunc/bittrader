package org.kentunc.bittrader.candle.feeder.domain.service

import org.kentunc.bittrader.candle.feeder.domain.repository.CandleRepository
import org.springframework.stereotype.Service

@Service
class CandleService(private val candleRepository: CandleRepository) : CandleRepository by candleRepository
