package org.kentunc.bittrader.web.presentation.advice

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute

@ControllerAdvice
class NavbarAdvice {

    @ModelAttribute("productCodes")
    fun productCodes() = ProductCode.values()
}
