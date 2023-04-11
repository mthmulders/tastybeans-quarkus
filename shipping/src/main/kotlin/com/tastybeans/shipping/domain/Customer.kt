package com.tastybeans.shipping.domain

import com.tastybeans.shipping.api.valueobjects.CustomerInfo
import io.quarkus.hibernate.reactive.panache.PanacheEntity
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheCompanionBase
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntityBase
import io.smallrye.mutiny.Uni
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class Customer() : PanacheEntityBase {
    @Id
    lateinit var id: String
    lateinit var firstName: String
    lateinit var lastName:  String
    lateinit var emailAddress:String
    @Embedded
    lateinit var shippingAddress: Address

    constructor(id: String, firstName: String, lastName: String, emailAddress: String, shippingAddress: Address) : this() {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.emailAddress = emailAddress
        this.shippingAddress = shippingAddress
    }

    companion object : PanacheCompanionBase<Customer, String> {
        fun findOrCreate(customerInfo: CustomerInfo): Uni<Customer> {
            return findById(customerInfo.customerId)
                .onItem().ifNull().switchTo {
                    Customer(
                        customerInfo.customerId,
                        customerInfo.firstName,
                        customerInfo.lastName,
                        customerInfo.emailAddress,
                        Address(
                            customerInfo.address.street,
                            customerInfo.address.houseNumber,
                            customerInfo.address.zipCode,
                            customerInfo.address.city
                        )
                    ).persist()
                }.map { item -> item!! }
        }
    }
}