package com.tastybeans.subscriptions.infrastructure

import com.tastybeans.subscriptions.api.events.SubscriptionCancelled
import com.tastybeans.subscriptions.api.events.SubscriptionEnded
import com.tastybeans.subscriptions.api.events.SubscriptionStarted
import io.smallrye.reactive.messaging.annotations.Merge
import io.smallrye.reactive.messaging.rabbitmq.OutgoingRabbitMQMetadata
import org.eclipse.microprofile.reactive.messaging.Acknowledgment
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.eclipse.microprofile.reactive.messaging.Message
import org.eclipse.microprofile.reactive.messaging.Metadata
import org.eclipse.microprofile.reactive.messaging.Outgoing
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class IntegrationEvents {
    @Merge
    @Incoming("subscriptionStarted")
    @Outgoing("subscriptionStartedIntegrationEvent")
    @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING)
    fun publishSubscriptionStarted(evt: SubscriptionStarted): Message<SubscriptionStarted> {
        val metadata = OutgoingRabbitMQMetadata.builder()
            .withRoutingKey("subscriptions.subscription.started.v1")
            .build()

        return Message.of(evt, Metadata.of(metadata))
    }

    @Incoming("subscriptionCancelled")
    @Outgoing("subscriptionCancelledIntegrationEvent")
    @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING)
    fun publishSubscriptionCancelled(evt: SubscriptionCancelled): Message<SubscriptionCancelled> {
        val metadata = OutgoingRabbitMQMetadata.builder()
            .withRoutingKey("subscriptions.subscription.cancelled.v1")
            .build()

        return Message.of(evt, Metadata.of(metadata))
    }

    @Incoming("subscriptionEnded")
    @Outgoing("subscriptionEndedIntegrationEvent")
    @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING)
    fun publishSubscriptionEnded(evt: SubscriptionEnded): Message<SubscriptionEnded> {
        val metadata = OutgoingRabbitMQMetadata.builder()
            .withRoutingKey("subscriptions.subscription.ended.v1")
            .build()

        return Message.of(evt, Metadata.of(metadata))
    }
}