package org.kentunc.bittrader.common.shared.extension

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

fun <T> Collection<T>.forEachParallel(f: suspend (T) -> Unit): Unit = runBlocking {
    map { async { f(it) } }.forEach { it.await() }
}

fun <T, U> List<T>.mapParallel(f: suspend (T) -> U): List<U> = runBlocking {
    map { async { f(it) } }.map { it.await() }
}
