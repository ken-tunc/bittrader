package org.kentunc.bittrader.candle.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.reactive.config.EnableWebFlux

@SpringBootApplication
@EnableWebFlux
class BittraderCandleApiApplication

fun main(args: Array<String>) {
	runApplication<BittraderCandleApiApplication>(*args)
}
