package com.tastybeans.subscriptions.domain

import com.tastybeans.subscriptions.api.commands.CancelSubscription
import com.tastybeans.subscriptions.api.commands.RegisterCustomer
import com.tastybeans.subscriptions.api.commands.StartSubscription
import com.tastybeans.subscriptions.api.exceptions.CustomerNotFoundException
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheCompanionBase
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntityBase
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import java.time.LocalDate
import javax.persistence.*
import javax.validation.ConstraintViolationException

@Entity
class Customer() : PanacheEntityBase {
    @Id
    lateinit var id: String
    lateinit var firstName: String
    lateinit var lastName: String
    lateinit var emailAddress: String

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "street", column = Column(name = "shippingStreet")),
        AttributeOverride(name = "houseNumber", column = Column(name = "shippingHouseNumber")),
        AttributeOverride(name = "zipCode", column = Column(name = "shippingZipcode")),
        AttributeOverride(name = "city", column = Column(name = "shippingCity"))
    )
    lateinit var shippingAddress: Address

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "street", column = Column(name = "invoiceStreet")),
        AttributeOverride(name = "houseNumber", column = Column(name = "invoiceHouseNumber")),
        AttributeOverride(name = "zipCode", column = Column(name = "invoiceZipcode")),
        AttributeOverride(name = "city", column = Column(name = "invoiceCity"))
    )
    lateinit var invoiceAddress: Address

    @Embedded
    var subscription: Subscription? = null

    private constructor(
        customerId: String,
        firstName: String,
        lastName: String,
        emailAddress: String,
        shippingAddress: Address,
        invoiceAddress: Address
    ) : this() {
        this.id = customerId
        this.firstName = firstName
        this.lastName = lastName
        this.emailAddress = emailAddress
        this.invoiceAddress = invoiceAddress
        this.shippingAddress = shippingAddress
    }

    fun cancelSubscription(cmd: CancelSubscription): Customer {
        val subscription = this.subscription ?: throw ConstraintViolationException(
            "Customer doesn't have an active subscription", emptySet()
        )

        this.subscription = subscription.copy(endDate = cmd.endDate)

        return this
    }

    fun endSubscription(): Customer {
        if (subscription?.endDate == null) {
            throw ConstraintViolationException(
                "The customer doesn't have a cancelled subscription",
                emptySet()
            )
        }

        this.subscription = null

        return this
    }

    fun startSubscription(cmd: StartSubscription): Customer {
        if (this.subscription != null && this.subscription?.endDate == null) {
            throw ConstraintViolationException("Customer already has a subscription", emptySet())
        }

        this.subscription = Subscription(cmd.startDate)

        return this
    }

    companion object : PanacheCompanionBase<Customer, String> {
        fun get(id: String): Uni<Customer> {
            return findById(id).onItem()
                .ifNull().failWith(CustomerNotFoundException())
                .map { customer -> customer!! }
        }

        fun findByEmailAddress(emailAddress: String): Uni<Customer?> {
            return find("emailAddress", emailAddress).firstResult()
        }

        fun register(cmd: RegisterCustomer): Uni<Customer> {
            val customer =
                Customer(
                    cmd.customerId,
                    cmd.firstName,
                    cmd.lastName,
                    cmd.emailAddress,
                    cmd.shippingAddress,
                    cmd.invoiceAddress
                )
            return customer.persist()
        }

        fun findWithPendingEndingSubscriptions(date: LocalDate): Multi<Customer> {
            return find("subscription.endDate is not null and subscription.endDate < ?1", date).stream()
        }
    }
}

@Embeddable
data class Address(var street: String, var houseNumber: String, var zipCode: String, var city: String)

@Embeddable
data class Subscription(var startDate: LocalDate, var endDate: LocalDate? = null)