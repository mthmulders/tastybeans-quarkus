package com.tastybeans.shipping.domain

import io.quarkus.hibernate.reactive.panache.PanacheEntity
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class OrderItem() : PanacheEntity() {
    var productId: Long = 0L
    lateinit var description: String
    var quantity: Int= 0

    constructor(productId : Long, description: String, quantity: Int) : this() {
        this.productId = productId
        this.description = description
        this.quantity = quantity
    }
}