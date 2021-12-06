package org.kentunc.bittrader.web

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BittraderWebApplication

fun main(args: Array<String>) {
    runApplication<BittraderWebApplication>(*args)
}
