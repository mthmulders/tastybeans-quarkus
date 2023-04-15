package com.tastybeans.ratings.application.eventhandlers

import com.tastybeans.ratings.api.commands.RegisterProduct
import com.tastybeans.ratings.api.events.ProductRegistered
import com.tastybeans.ratings.domain.product.Product
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.reactive.messaging.Incoming

class ProductRegisteredEventHandler {
    @Incoming("productRegistered")
    fun handle(evt: ProductRegistered): Uni<Void> {
        val cmd = RegisterProduct(evt.id, evt.name, evt.description, evt.unitPrice)
        return Product.register(cmd).replaceWithVoid()
    }
}