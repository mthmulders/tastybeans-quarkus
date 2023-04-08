package com.tastybeans.subscriptions.rest.forms

import com.tastybeans.subscriptions.domain.Address
import java.time.LocalDate

data class RegisterCustomerForm (
    val firstName: String,
    val lastName: String,
    val emailAddress: String,
    val shippingAddress: Address,
    val invoiceAddress: Address
)