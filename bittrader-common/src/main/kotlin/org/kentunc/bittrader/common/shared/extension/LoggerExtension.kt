package org.kentunc.bittrader.common.shared.extension

import org.slf4j.Logger
import org.slf4j.LoggerFactory

val Any.log: Logger
    get() = LoggerFactory.getLogger(this.javaClass)
