package com.tastybeans.catalog.resources

import com.tastybeans.catalog.domain.Product
import com.tastybeans.catalog.api.commands.RegisterProduct
import com.tastybeans.catalog.api.commands.RemoveProduct
import com.tastybeans.catalog.api.commands.UpdateProductDetails
import com.tastybeans.catalog.api.exceptions.ProductNotFoundException
import com.tastybeans.catalog.shared.PagedResult
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional
import io.smallrye.mutiny.Uni
import io.smallrye.reactive.messaging.MutinyEmitter
import org.eclipse.microprofile.reactive.messaging.Channel
import javax.transaction.Transactional
import javax.validation.ConstraintViolationException
import javax.ws.rs.core.Response
import javax.validation.Valid
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/products")
class ProductsResource {

    @Channel("registerProduct")
    internal lateinit var registerProductEmitter: MutinyEmitter<RegisterProduct>;

    @Channel("updateProductDetails")
    internal lateinit var updateProductDetailsEmitter: MutinyEmitter<UpdateProductDetails>;

    @Channel("removeProduct")
    internal lateinit var removeProduct: MutinyEmitter<RemoveProduct>;

    @GET
    @ReactiveTransactional
    @Produces(MediaType.APPLICATION_JSON)
    fun getAllProducts(@QueryParam("page") page: Int): Uni<PagedResult<Product>> {
        return Product.paged(page, 20)
    }

    @GET
    @Path("{id}")
    fun getProductDetails(id: Long): Uni<Response> {
        return Product.get(id)
                .map { product -> Response.ok(product).build() }
                .onFailure { t -> t is ProductNotFoundException }
                .recoverWithItem(Response.status(Response.Status.NOT_FOUND).build())
    }

    @POST
    @ReactiveTransactional
    @Consumes(MediaType.APPLICATION_JSON)
    fun registerProduct(@Valid entity: RegisterProduct): Uni<Response> {
        return registerProductEmitter.send(entity).replaceWith(Response.accepted().build())
                .onFailure().recoverWithItem(Response.status(400).build());
    }

    @PUT
    @Path("{id}")
    @ReactiveTransactional
    @Consumes(MediaType.APPLICATION_JSON)
    fun updateProductDetails(id: Long, entity: UpdateProductDetails): Uni<Response> {
        return updateProductDetailsEmitter.send(entity).replaceWith(Response.accepted().build())
                .onFailure { t -> t is ProductNotFoundException }.recoverWithItem(Response.status(404).build())
                .onFailure { t -> t is ConstraintViolationException }.recoverWithItem(Response.status(400).build())
    }

    @DELETE
    @ReactiveTransactional
    @Path("{id}")
    fun deleteProduct(id: Long): Uni<Response> {
        return removeProduct.send(RemoveProduct(id)).replaceWith(Response.status(Response.Status.NO_CONTENT).build())
                .onFailure { t -> t is ProductNotFoundException }.recoverWithItem(Response.status(Response.Status.NOT_FOUND).build())
                .onFailure { t -> t is ConstraintViolationException }.recoverWithItem(Response.status(400).build())
    }
}