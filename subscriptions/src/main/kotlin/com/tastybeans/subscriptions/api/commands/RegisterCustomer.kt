package com.tastybeans.subscriptions.api.commands

import com.tastybeans.subscriptions.domain.Address
import java.time.LocalDate

data class RegisterCustomer(
        val customerId: String,
        val firstName: String,
        val lastName: String,
        val emailAddress: String,
        val shippingAddress: Address,
        val invoiceAddress: Address,
        val registrationDate: LocalDate
)