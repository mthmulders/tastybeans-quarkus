package com.tastybeans.ratings.application.eventhandlers

import com.tastybeans.ratings.api.events.ProductRemoved
import com.tastybeans.ratings.domain.product.Product
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.reactive.messaging.Incoming

class ProductRemovedEventHandler {
    @Incoming("productRemoved")
    fun handle(evt: Uni<ProductRemoved>): Uni<Void> {
        return evt.flatMap { Product.get(it.id) }.map { it.delete() }.replaceWithVoid()
    }
}