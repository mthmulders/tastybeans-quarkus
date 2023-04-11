package com.tastybeans.shipping.api.commands

import com.tastybeans.shipping.api.valueobjects.CustomerInfo
import com.tastybeans.shipping.api.valueobjects.OrderItemInfo
import java.time.LocalDate

data class CreateOrder(val date: LocalDate, val customer: CustomerInfo, val orderItems: List<OrderItemInfo>)
