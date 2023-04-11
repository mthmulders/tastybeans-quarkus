package com.tastybeans.shipping.domain

import com.tastybeans.shipping.api.commands.CreateOrder
import com.tastybeans.shipping.api.exceptions.OrderNotFoundException
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntity
import io.smallrye.mutiny.Uni
import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity
class Order(): PanacheEntity() {
    lateinit var date: LocalDate
    @ManyToOne
    lateinit var customer: Customer
    @OneToMany
    lateinit var orderItems: MutableList<OrderItem>

    constructor(date: LocalDate, customer: Customer): this() {
        this.date = date
        this.customer = customer
    }

    companion object : PanacheCompanion<Order> {
        fun get(id: Long): Uni<Order> {
            return findById(id).onItem().ifNull().failWith(OrderNotFoundException()).map { item -> item!! }
        }

        fun create(cmd: CreateOrder): Uni<Order> {
            return Customer.findOrCreate(cmd.customer).chain { customer ->
                val order = Order(cmd.date, customer)
                order.orderItems.addAll(
                    cmd.orderItems.map { item ->
                        OrderItem(item.productId, item.description, item.quantity)
                    }.toList())

                order.persist<Order>().map { savedOrder ->
                    savedOrder!!
                }
            }
        }
    }
}