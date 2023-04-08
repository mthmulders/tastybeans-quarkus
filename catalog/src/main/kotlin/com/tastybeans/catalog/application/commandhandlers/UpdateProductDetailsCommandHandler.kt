package com.tastybeans.catalog.application.commandhandlers

import com.tastybeans.catalog.api.commands.UpdateProductDetails
import com.tastybeans.catalog.api.events.ProductDetailsUpdated
import com.tastybeans.catalog.domain.Product
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.eclipse.microprofile.reactive.messaging.Outgoing
import javax.enterprise.context.ApplicationScoped
import javax.validation.Valid

@ApplicationScoped
class UpdateProductDetailsCommandHandler {
    @Incoming("updateProductDetails")
    @Outgoing("productDetailsUpdated")
    fun handle(@Valid cmd: UpdateProductDetails): Uni<ProductDetailsUpdated> {
        return Product.get(cmd.id)
                .map { product -> product.updateDetails(cmd) }
                .map { product -> ProductDetailsUpdated(product.id!!, product.name, product.description, product.unitPrice) }
    }
}