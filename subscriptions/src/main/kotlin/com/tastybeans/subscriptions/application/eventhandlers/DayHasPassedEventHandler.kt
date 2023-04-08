package com.tastybeans.subscriptions.application.eventhandlers

import com.tastybeans.subscriptions.api.commands.EndSubscription
import com.tastybeans.subscriptions.api.events.DayHasPassed
import com.tastybeans.subscriptions.domain.Customer
import io.smallrye.mutiny.Multi
import io.vertx.core.json.JsonObject
import org.eclipse.microprofile.reactive.messaging.Acknowledgment
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.eclipse.microprofile.reactive.messaging.Outgoing
import org.slf4j.LoggerFactory
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.context.control.ActivateRequestContext

@ApplicationScoped
class DayHasPassedEventHandler {
    @ActivateRequestContext
    @Incoming("dayHasPassedIntegrationEvent")
    @Outgoing("endSubscription")
    @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING)
    fun handle(msg: JsonObject): Multi<EndSubscription> {
        val evt = msg.mapTo(DayHasPassed::class.java)

        return Customer.findWithPendingEndingSubscriptions(evt.date)
            .map { customer -> EndSubscription(customer.id) }
    }
}