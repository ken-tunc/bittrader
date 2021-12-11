package org.kentunc.bittrader.web.presentation.controller

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.common.domain.model.time.Duration
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/")
class IndexController {

    @ModelAttribute
    fun addAttributes(model: Model) {
        model.addAttribute("productCodes", ProductCode.values())
        model.addAttribute("durations", Duration.values())
    }

    @GetMapping
    suspend fun index(): String {
        return "index"
    }
}
