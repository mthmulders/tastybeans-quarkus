package com.tastybeans.catalog.infrastructure

import com.tastybeans.catalog.api.events.ProductDetailsUpdated
import com.tastybeans.catalog.api.events.ProductRegistered
import com.tastybeans.catalog.api.events.ProductRemoved
import io.smallrye.reactive.messaging.rabbitmq.OutgoingRabbitMQMetadata
import org.eclipse.microprofile.reactive.messaging.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class IntegrationEvents {
    @Incoming("productRegistered")
    @Outgoing("productRegisteredIntegrationEvent")
    @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING)
    fun publishProductRegistered(msg: ProductRegistered): Message<ProductRegistered> {
        val metadata = OutgoingRabbitMQMetadata.builder().withRoutingKey("catalog.product.registered.v1").build()
        return Message.of(msg, Metadata.of(metadata))
    }

    @Incoming("productDetailsUpdated")
    @Outgoing("productDetailsUpdatedIntegrationEvent")
    @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING)
    fun publishProductDetailsUpdated(evt: ProductDetailsUpdated): Message<ProductDetailsUpdated> {
        val metadata = OutgoingRabbitMQMetadata.builder().withRoutingKey("catalog.product.updated.v1").build()
        return Message.of(evt, Metadata.of(metadata))
    }

    @Incoming("productRemoved")
    @Outgoing("productRemovedIntegrationEvent")
    @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING)
    fun publishProductRemovedIntegrationEvent(evt: ProductRemoved): Message<ProductRemoved> {
        val metadata = OutgoingRabbitMQMetadata.builder().withRoutingKey("catalog.product.removed.v1").build()
        return Message.of(evt, Metadata.of(metadata))
    }
}