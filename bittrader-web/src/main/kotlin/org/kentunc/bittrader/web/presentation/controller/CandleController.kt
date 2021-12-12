package org.kentunc.bittrader.web.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.kentunc.bittrader.common.domain.model.candle.CandleQuery
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.web.application.CandleService
import org.kentunc.bittrader.web.presentation.model.CandleStick
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/candles")
class CandleController(private val candleService: CandleService, private val objectMapper: ObjectMapper) {

    @GetMapping("/{productCode}")
    suspend fun candlestickChart(
        @PathVariable productCode: ProductCode,
        @RequestParam(required = false) duration: Duration?,
        model: Model
    ): String {
        val activeDuration = duration ?: Duration.MINUTES
        val query = CandleQuery(productCode = productCode, duration = activeDuration, maxNum = 300)
        val candleSticks = candleService.search(query)
            .toList()
            .map { CandleStick.of(it) }
        model["productCode"] = productCode
        model["durations"] = Duration.values()
        model["activeDuration"] = activeDuration
        model["candleSticks"] = objectMapper.writeValueAsString(candleSticks)
        return "candle"
    }
}
