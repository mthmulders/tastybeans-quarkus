package com.tastybeans.shipping.api.valueobjects

import com.tastybeans.shipping.domain.Address

data class AddressInfo(val street: String, val houseNumber: String, val zipCode: String, val city: String) {
    companion object {
        fun from(address: Address): AddressInfo {
            return AddressInfo(address.street, address.houseNumber, address.zipCode, address.city)
        }
    }
}