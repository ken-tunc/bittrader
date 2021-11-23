package org.kentunc.bittrader.common.domain.model.market

enum class ProductCode(val left: CurrencyCode, val right: CurrencyCode) {
    BTC_JPY(CurrencyCode.BTC, CurrencyCode.JPY),
    ETH_JPY(CurrencyCode.ETH, CurrencyCode.JPY);
}
