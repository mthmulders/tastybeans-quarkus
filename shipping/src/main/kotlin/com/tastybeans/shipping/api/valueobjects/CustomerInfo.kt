package com.tastybeans.shipping.api.valueobjects

import com.tastybeans.shipping.domain.Customer

data class CustomerInfo(
    val customerId: String,
    val firstName: String,
    val lastName: String,
    val emailAddress: String,
    val address: AddressInfo
) {
    companion object {
        fun from(customer: Customer): CustomerInfo {
            return CustomerInfo(
                customer.id,
                customer.firstName,
                customer.lastName,
                customer.emailAddress,
                AddressInfo.from(customer.shippingAddress)
            )
        }
    }
}
