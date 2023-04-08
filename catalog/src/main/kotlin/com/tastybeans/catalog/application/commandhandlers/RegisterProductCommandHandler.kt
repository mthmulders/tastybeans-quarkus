package com.tastybeans.catalog.application.commandhandlers

import com.tastybeans.catalog.api.commands.RegisterProduct
import com.tastybeans.catalog.api.events.ProductRegistered
import com.tastybeans.catalog.domain.Product
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.eclipse.microprofile.reactive.messaging.Outgoing
import javax.enterprise.context.ApplicationScoped
import javax.validation.Valid

@ApplicationScoped
class RegisterProductCommandHandler {
    @Incoming("registerProduct")
    @Outgoing("productRegistered")
    fun handle(@Valid cmd: RegisterProduct): Uni<ProductRegistered> {
        return Product.register(cmd).map { product -> ProductRegistered(product.id!!, product.name, product.description, product.unitPrice) }
    }
}