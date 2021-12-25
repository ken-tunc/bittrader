package org.kentunc.bittrader.common.infrastructure.webclient.http.bitflyer.model.market

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class CommissionRateResponseTest {

    @Test
    fun testToCommissionRate() {
        val response = CommissionRateResponse(BigDecimal.valueOf(123.4))
        val actual = response.toCommissionRate()
        assertEquals(response.commissionRate, actual.toBigDecimal())
    }
}
