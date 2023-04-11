package com.tastybeans.shipping.domain

import javax.persistence.Embeddable

@Embeddable
data class Address(var street: String, var houseNumber: String, var zipCode: String, var city: String)
