package org.kentunc.bittrader.common.presentation.model.market

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kentunc.bittrader.common.domain.model.market.CommissionRate

internal class CommissionRateResponseTest {

    @Test
    fun testOf() {
        val commissionRate = CommissionRate.of(100.0)
        val actual = CommissionRateResponse.of(commissionRate)
        assertEquals(commissionRate.toBigDecimal(), actual.commissionRate)
    }

    @Test
    fun testToCommissionRate() {
        val commissionRate = CommissionRate.of(100.0)
        val commissionRateResponse = CommissionRateResponse(commissionRate.toBigDecimal())
        val actual = commissionRateResponse.toCommissionRate()
        assertEquals(commissionRate, actual)
    }
}
