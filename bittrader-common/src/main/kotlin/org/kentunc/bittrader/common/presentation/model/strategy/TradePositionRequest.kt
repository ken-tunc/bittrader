package org.kentunc.bittrader.common.presentation.model.strategy

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.server.ServerWebInputException

data class TradePositionRequest(val productCode: ProductCode, val duration: Duration) {

    companion object {
        private const val PRODUCT_CODE_KEY = "product_code"
        private const val DURATION_KEY = "duration"

        fun from(params: MultiValueMap<String, String>): TradePositionRequest {
            val productCode = runCatching {
                params.getFirst(PRODUCT_CODE_KEY)!!.let { ProductCode.valueOf(it) }
            }.getOrElse { throw ServerWebInputException("invalid product code.") }

            val duration = runCatching {
                params.getFirst(DURATION_KEY)!!.let { Duration.valueOf(it) }
            }.getOrElse { throw ServerWebInputException("invalid duration.") }

            return TradePositionRequest(productCode, duration)
        }
    }

    fun toMultiValueMap(): MultiValueMap<String, String> {
        return LinkedMultiValueMap<String, String>().apply {
            add(PRODUCT_CODE_KEY, productCode.toString())
            add(DURATION_KEY, duration.toString())
        }
    }
}
