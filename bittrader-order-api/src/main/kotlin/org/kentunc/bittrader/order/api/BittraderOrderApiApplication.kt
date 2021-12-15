package org.kentunc.bittrader.order.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.reactive.config.EnableWebFlux

@EnableWebFlux
@SpringBootApplication
class BittraderOrderApiApplication

fun main(args: Array<String>) {
    runApplication<BittraderOrderApiApplication>(*args)
}
