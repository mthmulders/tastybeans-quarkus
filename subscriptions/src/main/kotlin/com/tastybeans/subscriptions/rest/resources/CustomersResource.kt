package com.tastybeans.subscriptions.rest.resources

import com.tastybeans.subscriptions.api.commands.CancelSubscription
import com.tastybeans.subscriptions.api.commands.RegisterCustomer
import com.tastybeans.subscriptions.api.commands.StartSubscription
import com.tastybeans.subscriptions.api.exceptions.CustomerNotFoundException
import com.tastybeans.subscriptions.domain.Customer
import com.tastybeans.subscriptions.rest.forms.RegisterCustomerForm
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional
import io.smallrye.mutiny.Uni
import io.smallrye.reactive.messaging.MutinyEmitter
import org.eclipse.microprofile.reactive.messaging.Channel
import java.net.URI
import java.time.LocalDate
import java.util.*
import javax.validation.ConstraintViolationException
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/customers")
class CustomersResource {
    @Channel("registerCustomer")
    internal lateinit var registerCustomerEmitter: MutinyEmitter<RegisterCustomer>

    @Channel("cancelSubscription")
    internal lateinit var cancelSubscriptionEmitter: MutinyEmitter<CancelSubscription>

    @Channel("startSubscription")
    internal lateinit var startSubscriptionEmitter: MutinyEmitter<StartSubscription>

    @GET
    @Path("{customerId}")
    @ReactiveTransactional
    @Produces(MediaType.APPLICATION_JSON)
    fun getCustomerDetails(@PathParam("customerId") customerId: String): Uni<Response> {
        return Customer.findById(customerId)
            .map { customer -> Response.ok(customer).build() }
            .onFailure { t -> t is CustomerNotFoundException }.recoverWithItem(Response.status(Response.Status.NOT_FOUND).build())
    }

    @POST
    @ReactiveTransactional
    @Consumes(MediaType.APPLICATION_JSON)
    fun registerCustomer(entity: RegisterCustomerForm): Uni<Response> {
        val customerId: String = UUID.randomUUID().toString()

        val cmd = RegisterCustomer(
            customerId,
            entity.firstName,
            entity.lastName,
            entity.emailAddress,
            entity.shippingAddress,
            entity.invoiceAddress,
            LocalDate.now()
        )

        return registerCustomerEmitter.send(cmd)
            .replaceWith(Response.created(URI.create("/customers/${customerId}")).build())
            .onFailure { t -> t is ConstraintViolationException }.recoverWithItem(Response.status(Response.Status.BAD_REQUEST).build())
    }

    @POST
    @ReactiveTransactional
    @Path("{customerId}/subscription/cancel")
    fun cancelSubscription(@PathParam("customerId") customerId: String): Uni<Response> {
        return cancelSubscriptionEmitter.send(CancelSubscription(customerId, LocalDate.now()))
            .map { Response.status(Response.Status.ACCEPTED).build() }
            .onFailure { t -> t is CustomerNotFoundException }.recoverWithItem(Response.status(Response.Status.NOT_FOUND).build())
            .onFailure { t -> t is ConstraintViolationException }.recoverWithItem(Response.status(Response.Status.BAD_REQUEST).build())
    }

    @POST
    @ReactiveTransactional
    @Path("{customerId}/subscription/resume")
    fun startSubscription(@PathParam("customerId") customerId: String): Uni<Response> {
        return startSubscriptionEmitter.send(StartSubscription(customerId, LocalDate.now()))
            .map { Response.status(Response.Status.ACCEPTED).build() }
            .onFailure { t -> t is CustomerNotFoundException }.recoverWithItem(Response.status(Response.Status.NOT_FOUND).build())
            .onFailure { t -> t is ConstraintViolationException }.recoverWithItem(Response.status(Response.Status.BAD_REQUEST).build())
    }
}