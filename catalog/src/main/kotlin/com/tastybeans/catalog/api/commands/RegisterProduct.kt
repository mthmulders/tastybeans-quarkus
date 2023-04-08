package com.tastybeans.catalog.api.commands

import java.math.BigDecimal

data class RegisterProduct(val name: String, val description: String, val unitPrice: BigDecimal)