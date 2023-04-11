package com.tastybeans.shipping.application.commandhandlers

import com.tastybeans.shipping.api.events.OrderCreated
import com.tastybeans.shipping.api.commands.CreateOrder
import com.tastybeans.shipping.api.valueobjects.CustomerInfo
import com.tastybeans.shipping.api.valueobjects.OrderItemInfo
import com.tastybeans.shipping.domain.Order
import org.eclipse.microprofile.reactive.messaging.Outgoing
import org.eclipse.microprofile.reactive.messaging.Incoming
import javax.enterprise.context.ApplicationScoped
import io.smallrye.mutiny.Uni

@ApplicationScoped
class CreateOrderCommandHandler {
    @Incoming("createOrder")
    @Outgoing("orderCreated")
    fun handle(cmd: CreateOrder): Uni<OrderCreated> {
        return Order.create(cmd).map { order ->
            val customerInfo = CustomerInfo.from(order.customer)
            val orderItems = order.orderItems.map { item -> OrderItemInfo.from(item) }
            OrderCreated(order.id!!, order.date, customerInfo, orderItems)
        }
    }
}