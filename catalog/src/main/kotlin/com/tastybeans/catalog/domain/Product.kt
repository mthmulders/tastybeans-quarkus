package com.tastybeans.catalog.domain

import com.tastybeans.catalog.api.commands.RegisterProduct
import com.tastybeans.catalog.api.commands.UpdateProductDetails
import com.tastybeans.catalog.api.exceptions.ProductNotFoundException
import com.tastybeans.catalog.shared.PagedResult
import io.quarkus.hibernate.reactive.panache.Panache
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntity
import io.smallrye.mutiny.Uni
import java.math.BigDecimal
import javax.persistence.Entity

@Entity
class Product() : PanacheEntity() {
    lateinit var name: String
    lateinit var description: String
    lateinit var unitPrice: BigDecimal

    private constructor(name: String, description: String, unitPrice: BigDecimal) : this() {
        this.name = name
        this.description = description
        this.unitPrice = unitPrice
    }

    fun updateDetails(cmd: UpdateProductDetails): Product {
        this.name = cmd.name
        this.description = cmd.description
        this.unitPrice = cmd.unitPrice

        return this
    }

    companion object : PanacheCompanion<Product> {
        fun paged(pageIndex: Int, pageSize: Int): Uni<PagedResult<Product>> {
            val total = count()
            val items = findAll().page(pageIndex, pageSize).list()

            return Uni.combine().all().unis(total, items).asTuple().map {
                PagedResult<Product>(it.item2, pageIndex, pageSize, it.item1)
            }
        }

        fun register(cmd: RegisterProduct): Uni<Product> {
            val product = Product(cmd.name, cmd.description, cmd.unitPrice)
            return product.persist()
        }

        fun get(id: Long): Uni<Product> {
            return findById(id)
                    .onItem().ifNull().failWith(ProductNotFoundException())
                    .map { result -> result!! }
        }
    }
}