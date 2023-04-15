package com.tastybeans.ratings.infrastructure

import com.tastybeans.ratings.api.events.ProductRemoved
import com.tastybeans.ratings.api.events.ProductRegistered
import io.smallrye.mutiny.Uni
import io.vertx.core.json.JsonObject
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.eclipse.microprofile.reactive.messaging.Outgoing

class IntegrationEvents {
    @Incoming("productRegisteredIntegrationEvent")
    @Outgoing("productRegistered")
    fun handleProductRegistered(evt: Uni<JsonObject>): Uni<ProductRegistered> {
        return evt.map { it.mapTo(ProductRegistered::class.java) }
    }

    @Incoming("productRemovedIntegrationEvent")
    @Outgoing("productRemoved")
    fun handleProductRemoved(evt: Uni<JsonObject>): Uni<ProductRemoved> {
        return evt.map { it.mapTo(ProductRemoved::class.java) }
    }
}