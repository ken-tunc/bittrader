package org.kentunc.bittrader.common.domain.model.quote

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

internal class PriceTest {

    @Test
    fun testPlus() {
        val price = Price.of(100.0)
        val augend = Price.of(234.56789)
        val expected = Price.of(334.56789)
        assertEquals(expected, price + augend)
    }

    @Test
    fun testMinus() {
        val price = Price.of(1000.0)
        val subtrahend = Price.of(234.56789)
        val expected = Price.of(765.43211)
        assertEquals(expected, price - subtrahend)
    }

    @Test
    fun testTimes() {
        val price = Price.of(123.4)
        val multiplicand = Price.of(11.1)
        val expected = Price.of(1369.74)
        assertEquals(expected, price * multiplicand)
    }

    @Test
    fun testDiv() {
        val price = Price.of(123.4)
        val divisor = Price.of(20.0)
        val expected = Price.of(6.17)
        assertEquals(expected, price / divisor)
    }

    @Test
    fun testCompare() {
        val price = Price.of(100.0)
        assertAll(
            { assertTrue(price > Price.of(99.9999)) },
            { assertTrue(price == Price.of(100.0)) },
            { assertTrue(price < Price.of(100.0001)) }
        )
    }
}
