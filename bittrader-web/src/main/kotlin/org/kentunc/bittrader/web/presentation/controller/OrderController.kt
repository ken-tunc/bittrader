package org.kentunc.bittrader.web.presentation.controller

import org.kentunc.bittrader.common.domain.model.market.ProductCode
import org.kentunc.bittrader.web.application.OrderService
import org.kentunc.bittrader.web.presentation.model.OrderRow
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/orders")
class OrderController(private val orderService: OrderService) {

    @GetMapping("/{productCode}")
    suspend fun orders(@PathVariable productCode: ProductCode, model: Model): String {
        val orders = orderService.find(productCode)
            .toList()
            .map { OrderRow.of(it) }
        model.addAttribute("orders", orders)
        return "orders"
    }
}
