package com.tastybeans.catalog.api.events

import java.math.BigDecimal

data class ProductRegistered(val id: Long, val name: String, val description: String, val unitPrice: BigDecimal)