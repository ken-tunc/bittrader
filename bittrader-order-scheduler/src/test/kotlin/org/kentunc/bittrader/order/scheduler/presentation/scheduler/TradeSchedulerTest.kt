package org.kentunc.bittrader.order.scheduler.presentation.scheduler

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coVerify
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.kentunc.bittrader.order.scheduler.application.TradeInteractor
import org.kentunc.bittrader.order.scheduler.presentation.configuration.SchedulerConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@ActiveProfiles("scheduling")
@SpringJUnitConfig(
    classes = [SchedulerConfiguration::class],
    initializers = [ConfigDataApplicationContextInitializer::class]
)
internal class TradeSchedulerTest {

    @MockkBean(relaxed = true)
    private lateinit var tradeInteractor: TradeInteractor

    @Autowired
    private lateinit var target: TradeScheduler

    @Test
    fun testScheduleTrade() {
        target.scheduleTrade()
        coVerify { tradeInteractor.trade(ProductCode.BTC_JPY, Duration.MINUTES) }
    }

    @Test
    fun testOptimizeStrategies() {
        target.scheduleOptimizeStrategies()
        coVerify { tradeInteractor.optimizeStrategies(ProductCode.BTC_JPY, Duration.MINUTES) }
    }
}
