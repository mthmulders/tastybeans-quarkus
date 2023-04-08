package com.tastybeans.subscriptions.application.commandhandlers

import com.tastybeans.subscriptions.api.commands.RegisterCustomer
import com.tastybeans.subscriptions.api.events.SubscriptionStarted
import com.tastybeans.subscriptions.domain.Customer
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.eclipse.microprofile.reactive.messaging.Outgoing
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class RegisterCustomerCommandHandler {
    @Incoming("registerCustomer")
    @Outgoing("subscriptionStarted")
    fun handle(cmd: RegisterCustomer): Uni<SubscriptionStarted> {
        return Customer.register(cmd).map { customer ->
            SubscriptionStarted(
                customer.id,
                customer.firstName,
                customer.lastName,
                customer.emailAddress,
                customer.shippingAddress,
                customer.invoiceAddress,
                cmd.registrationDate
            )
        }
    }
}