package org.kentunc.bittrader.candle.feeder.infrastructure.repository

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.kentunc.bittrader.candle.feeder.domain.exception.UnexpectedSubscribeException
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.infrastructure.webclient.websocket.bitflyer.BitflyerRealtimeTickerClient
import org.kentunc.bittrader.common.infrastructure.webclient.websocket.bitflyer.model.TickerMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import java.math.BigDecimal
import java.time.Instant

@SpringJUnitConfig(TickerRepositoryImpl::class)
internal class TickerRepositoryImplTest {

    @MockkBean
    private lateinit var bitflyerRealtimeTickerClient: BitflyerRealtimeTickerClient

    @Autowired
    private lateinit var target: TickerRepositoryImpl

    @Test
    fun testSubscribe() = runBlocking {
        // setup:
        val productCode = ProductCode.BTC_JPY
        val ticker1 = TickerMessage(
            productCode = productCode,
            timestamp = Instant.now().toString(),
            bestBid = BigDecimal.valueOf(100.0),
            bestAsk = BigDecimal.valueOf(100.0),
            volume = BigDecimal.valueOf(100.0)
        )
        val ticker2 = TickerMessage(
            productCode = productCode,
            timestamp = Instant.now().toString(),
            bestBid = BigDecimal.valueOf(100.0),
            bestAsk = BigDecimal.valueOf(100.0),
            volume = BigDecimal.valueOf(100.0)
        )
        coEvery { bitflyerRealtimeTickerClient.subscribe(productCode) } returns flowOf(ticker1, ticker2)

        // exercise:
        val actual = target.subscribe(productCode).toList()

        // verify:
        assertAll(
            { assertEquals(2, actual.size) },
            { assertEquals(ticker1.toTicker().id, actual[0].id) },
            { assertEquals(ticker2.toTicker().id, actual[1].id) },
        )
    }

    @Test
    fun testSubscribe_thrown(): Unit = runBlocking {
        // setup:
        coEvery { bitflyerRealtimeTickerClient.subscribe(any()) } throws RuntimeException("test")

        // exercise & verify:
        assertThrows<UnexpectedSubscribeException> {
            target.subscribe(ProductCode.BTC_JPY)
        }
    }
}
