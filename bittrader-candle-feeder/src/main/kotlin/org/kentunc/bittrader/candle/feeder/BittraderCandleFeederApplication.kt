package org.kentunc.bittrader.candle.feeder

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
class BittraderCandleFeederApplication

fun main(args: Array<String>) {
    runApplication<BittraderCandleFeederApplication>(*args)
}
