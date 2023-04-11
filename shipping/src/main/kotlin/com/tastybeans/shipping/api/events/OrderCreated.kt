package com.tastybeans.shipping.api.events

import com.tastybeans.shipping.api.valueobjects.CustomerInfo
import com.tastybeans.shipping.api.valueobjects.OrderItemInfo
import java.time.LocalDate

data class OrderCreated(
    val orderId: Long,
    val date: LocalDate,
    val customer: CustomerInfo,
    val orderItems: List<OrderItemInfo>
)
