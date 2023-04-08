package com.tastybeans.subscriptions.application.commandhandlers

import com.tastybeans.subscriptions.api.commands.StartSubscription
import com.tastybeans.subscriptions.api.events.SubscriptionStarted
import com.tastybeans.subscriptions.domain.Customer
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.eclipse.microprofile.reactive.messaging.Outgoing
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class StartSubscriptionCommandHandler {
    @Incoming("startSubscription")
    @Outgoing("subscriptionResumed")
    fun handle(cmd: StartSubscription): Uni<SubscriptionStarted> {
        return Customer.get(cmd.customerId).map { customer ->
            customer.startSubscription(cmd)
            SubscriptionStarted(
                customer.id,
                customer.firstName,
                customer.lastName,
                customer.emailAddress,
                customer.shippingAddress,
                customer.invoiceAddress,
                cmd.startDate
            )
        }
    }
}