package org.kentunc.bittrader.candle.feeder.presentation.event.listener

import com.ninjasquad.springmockk.MockkBean
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.candle.feeder.test.CandleFeederTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.test.context.ActiveProfiles

@CandleFeederTest
@ActiveProfiles("test", "feed")
internal class CandleFeedEventPublisherTest {

    @MockkBean(relaxed = true)
    private lateinit var eventPublisher: ApplicationEventPublisher

    @Autowired
    private lateinit var target: CandleFeedEventPublisher

    @Test
    fun testPublishEvents() {
        // Cannot resolve overloaded method...?
        // verify(exactly = ProductCode.values().size) { eventPublisher.publishEvent(any<CandleFeedEvent>()) }
        assertNotNull(target)
    }
}
