package org.kentunc.bittrader.common.test.model

import org.kentunc.bittrader.common.domain.model.market.Balance
import org.kentunc.bittrader.common.domain.model.market.CommissionRate
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.order.MinutesToExpire
import org.kentunc.bittrader.common.domain.model.order.Order
import org.kentunc.bittrader.common.domain.model.order.TimeInForce
import org.kentunc.bittrader.common.domain.model.quote.Size

object TestOrder {

    fun createBuyAll(
        productCode: ProductCode = ProductCode.BTC_JPY,
        balance: Size = Size.of(100.0),
        commissionRate: CommissionRate = CommissionRate.of(0.0015),
        minutesToExpire: MinutesToExpire = MinutesToExpire.of(10),
        timeInForce: TimeInForce = TimeInForce.GTC
    ): Order {
        return Order.ofBuyAll(
            productCode = productCode,
            balance = Balance.of(productCode.right, balance, balance),
            commissionRate = commissionRate,
            minutesToExpire = minutesToExpire,
            timeInForce = timeInForce
        )
    }

    fun createSellAll(
        productCode: ProductCode = ProductCode.BTC_JPY,
        balance: Size = Size.of(100.0),
        minutesToExpire: MinutesToExpire = MinutesToExpire.of(10),
        timeInForce: TimeInForce = TimeInForce.GTC
    ): Order {
        return Order.ofSellAll(
            productCode = productCode,
            balance = Balance.of(productCode.left, balance, balance),
            minutesToExpire = minutesToExpire,
            timeInForce = timeInForce
        )
    }
}
