package com.tastybeans.ratings.api.events

import java.math.BigDecimal

data class ProductDetailsUpdated(val id: Long, val name: String, val description: String, val unitPrice: BigDecimal) {
}