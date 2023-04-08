package com.tastybeans.catalog.application.commandhandlers

import com.tastybeans.catalog.api.commands.RemoveProduct
import com.tastybeans.catalog.api.events.ProductRemoved
import com.tastybeans.catalog.domain.Product
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.eclipse.microprofile.reactive.messaging.Outgoing
import javax.enterprise.context.ApplicationScoped
import javax.validation.Valid

@ApplicationScoped
class RemoveProductCommandHandler {
    @Incoming("removeProduct")
    @Outgoing("productRemoved")
    fun handle(@Valid cmd: RemoveProduct): Uni<ProductRemoved> {
        return Product.get(cmd.id)
                .map { product -> product.delete() }
                .replaceWith(ProductRemoved(cmd.id))
    }
}