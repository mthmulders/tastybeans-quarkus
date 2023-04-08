package com.tastybeans.subscriptions.application.eventhandlers

import com.tastybeans.subscriptions.api.commands.EndSubscription
import com.tastybeans.subscriptions.api.events.DayHasPassed
import com.tastybeans.subscriptions.domain.Customer
import io.smallrye.mutiny.Multi
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.eclipse.microprofile.reactive.messaging.Outgoing
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class DayHasPassedEventHandler {
    @Incoming("dayHasPassedIntegrationEvent")
    @Outgoing("endSubscription")
    fun handle(evt: DayHasPassed): Multi<EndSubscription> {
        return Customer.findWithPendingEndingSubscriptions(evt.date)
            .map { customer -> EndSubscription(customer.id) }
    }
}