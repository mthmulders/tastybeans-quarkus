package com.tastybeans.subscriptions.api.events

import com.tastybeans.subscriptions.domain.Address
import java.time.LocalDate

data class SubscriptionStarted(
        val customerId : String,
        val firstName: String,
        val lastName: String,
        val emailAddress: String,
        val shippingAddress: Address,
        val invoiceAddress: Address,
        val startDate: LocalDate
)
