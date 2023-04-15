package com.tastybeans.ratings.domain.product

import com.tastybeans.catalog.api.exceptions.ProductNotFoundException
import com.tastybeans.ratings.api.commands.RegisterProduct
import com.tastybeans.ratings.api.commands.UpdateProductDetails
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntity
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheCompanionBase
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntityBase
import io.smallrye.mutiny.Uni
import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class Product() : PanacheEntityBase {
    @Id
    var id: Long = 0
    lateinit var name: String
    lateinit var description: String
    lateinit var unitPrice: BigDecimal

    private constructor(id: Long, name: String, description: String, unitPrice: BigDecimal) : this() {
        this.id = id
        this.name = name
        this.description = description
        this.unitPrice = unitPrice
    }

    fun updateDetails(cmd: UpdateProductDetails) {
        this.name = cmd.name
        this.description = cmd.description
        this.unitPrice = cmd.unitPrice
    }

    companion object : PanacheCompanionBase<Product, Long> {
        fun register(cmd: RegisterProduct): Uni<Product> {
            val product = Product(cmd.id, cmd.name, cmd.description, cmd.unitPrice)
            return product.persist()
        }

        fun get(id: Long): Uni<Product> {
            return findById(id)
                .onItem().ifNull().failWith(ProductNotFoundException())
                .map { result -> result!! }
        }
    }
}