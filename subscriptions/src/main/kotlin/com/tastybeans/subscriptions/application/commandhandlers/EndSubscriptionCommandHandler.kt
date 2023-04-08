package com.tastybeans.subscriptions.application.commandhandlers

import com.tastybeans.subscriptions.api.commands.EndSubscription
import com.tastybeans.subscriptions.api.events.SubscriptionEnded
import com.tastybeans.subscriptions.domain.Customer
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.eclipse.microprofile.reactive.messaging.Outgoing
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class EndSubscriptionCommandHandler {
    @Incoming("endSubscription")
    @Outgoing("subscriptionEnded")
    fun handle(cmd: EndSubscription): Uni<SubscriptionEnded> {
        return Customer.get(cmd.customerId).map { customer ->
            customer.endSubscription()
            SubscriptionEnded(customer.id)
        }
    }
}