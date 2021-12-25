package org.kentunc.bittrader.web.presentation.controller

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.kentunc.bittrader.web.application.BalanceService
import org.kentunc.bittrader.web.presentation.model.BalanceRow
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/balances")
class BalanceController(private val balanceService: BalanceService) {

    @GetMapping
    suspend fun balances(model: Model): String {
        val balances = balanceService.get()
            .map { BalanceRow.of(it) }
            .toList()
        model.addAttribute("balances", balances)
        return "balances"
    }
}
