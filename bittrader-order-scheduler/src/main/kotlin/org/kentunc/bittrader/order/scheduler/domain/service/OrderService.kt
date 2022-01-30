package org.kentunc.bittrader.order.scheduler.domain.service

import org.kentunc.bittrader.order.scheduler.domain.repository.OrderRepository
import org.springframework.stereotype.Service

@Service
class OrderService(private val orderRepository: OrderRepository) : OrderRepository by orderRepository
