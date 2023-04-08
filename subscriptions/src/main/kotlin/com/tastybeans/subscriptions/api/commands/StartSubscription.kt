package com.tastybeans.subscriptions.api.commands

import java.time.LocalDate

data class StartSubscription(val customerId: String, val startDate: LocalDate) {
}