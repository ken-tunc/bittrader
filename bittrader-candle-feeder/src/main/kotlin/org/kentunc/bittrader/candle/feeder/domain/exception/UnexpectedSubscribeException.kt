package org.kentunc.bittrader.candle.feeder.domain.exception

class UnexpectedSubscribeException : RuntimeException {

    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable) : super(message, cause)
}
