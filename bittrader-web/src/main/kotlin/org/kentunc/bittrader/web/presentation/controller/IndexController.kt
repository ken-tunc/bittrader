package org.kentunc.bittrader.web.presentation.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/")
class IndexController {

    @GetMapping
    suspend fun index(): String {
        return "index"
    }
}
