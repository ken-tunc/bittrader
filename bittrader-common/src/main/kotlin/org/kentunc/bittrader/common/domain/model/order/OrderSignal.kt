package org.kentunc.bittrader.common.domain.model.order

import org.kentunc.bittrader.common.domain.model.market.Balance
import org.kentunc.bittrader.common.domain.model.market.CommissionRate
import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.ticker.Ticker

class OrderSignal private constructor(
    val detail: OrderDetail,
    val minutesToExpire: MinutesToExpire,
    val timeInForce: TimeInForce
) {

    companion object {
        private val DEFAULT_EXPIRE_MINUTES = MinutesToExpire.of(10)

        fun of(detail: OrderDetail, minutesToExpire: MinutesToExpire, timeInForce: TimeInForce) =
            OrderSignal(detail, minutesToExpire, timeInForce)

        fun ofSellAll(
            productCode: ProductCode,
            balance: Balance,
            minutesToExpire: MinutesToExpire = DEFAULT_EXPIRE_MINUTES,
            timeInForce: TimeInForce = TimeInForce.GTC
        ): OrderSignal {
            require(productCode.left == balance.currencyCode) {
                "Invalid sell order: productCode=$productCode, balance currencyCode=${balance.currencyCode}"
            }
            val detail = OrderDetail.of(
                productCode = productCode,
                orderType = OrderType.MARKET,
                orderSide = OrderSide.SELL,
                price = null,
                size = balance.available
            )
            return OrderSignal(
                detail = detail,
                minutesToExpire = minutesToExpire,
                timeInForce = timeInForce
            )
        }

        fun ofBuyAll(
            productCode: ProductCode,
            balance: Balance,
            commissionRate: CommissionRate,
            ticker: Ticker,
            minutesToExpire: MinutesToExpire = DEFAULT_EXPIRE_MINUTES,
            timeInForce: TimeInForce = TimeInForce.GTC
        ): OrderSignal {
            require(productCode.right == balance.currencyCode) {
                "Invalid buy order: productCode=$productCode, balance currencyCode=${balance.currencyCode}"
            }
            require(productCode == ticker.id.productCode) {
                "Invalid buy order: productCode=$productCode, ticker productCode=${ticker.id.productCode}"
            }
            val price = ticker.bestAsk
            val buySize = balance.buySize(ticker.bestAsk)
            val adjustedSize = buySize - commissionRate.fee(buySize)
            val detail = OrderDetail.of(
                productCode = productCode,
                orderType = OrderType.LIMIT,
                orderSide = OrderSide.BUY,
                price = price,
                size = adjustedSize
            )
            return OrderSignal(
                detail = detail,
                minutesToExpire = minutesToExpire,
                timeInForce = timeInForce
            )
        }
    }
}
