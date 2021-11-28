package org.kentunc.bittrader.candle.feeder.domain.service

import org.kentunc.bittrader.candle.feeder.domain.repository.TickerRepository
import org.springframework.stereotype.Service

@Service
class TickerService(private val tickerRepository: TickerRepository) : TickerRepository by tickerRepository
