package org.kentunc.bittrader.candle.feeder.presentation.event.listener

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coVerify
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.kentunc.bittrader.candle.feeder.application.CandleFeedInteractor
import org.kentunc.bittrader.candle.feeder.presentation.event.CandleFeedEvent
import org.kentunc.bittrader.candle.feeder.test.CandleFeederTest
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher

@CandleFeederTest
internal class CandleFeedEventListenerTest {

    @MockkBean(relaxUnitFun = true)
    private lateinit var candleFeedInteractor: CandleFeedInteractor

    @Autowired
    private lateinit var eventPublisher: ApplicationEventPublisher

    @ParameterizedTest
    @EnumSource(ProductCode::class)
    fun testFeedCandles(productCode: ProductCode) {
        // exercise:
        eventPublisher.publishEvent(CandleFeedEvent(productCode))

        // verify:
        coVerify { candleFeedInteractor.feedCandles(productCode) }
    }
}
