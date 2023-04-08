package com.tastybeans.subscriptions.application.commandhandlers

import com.tastybeans.subscriptions.api.commands.CancelSubscription
import com.tastybeans.subscriptions.api.events.SubscriptionCancelled
import com.tastybeans.subscriptions.domain.Customer
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.eclipse.microprofile.reactive.messaging.Outgoing
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class CancelSubscriptionCommandHandler {
    @Incoming("cancelSubscription")
    @Outgoing("subscriptionCancelled")
    fun handle(cmd: CancelSubscription): Uni<SubscriptionCancelled> {
        return Customer.get(cmd.customerId)
            .map { customer ->
                customer.cancelSubscription(cmd)
                SubscriptionCancelled(cmd.customerId, cmd.endDate)
            }
    }
}