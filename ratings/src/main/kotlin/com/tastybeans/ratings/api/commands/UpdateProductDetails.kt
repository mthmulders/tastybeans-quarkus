package com.tastybeans.ratings.api.commands

import java.math.BigDecimal

data class UpdateProductDetails(val id: Long, val name: String, val description: String, val unitPrice: BigDecimal) {
}