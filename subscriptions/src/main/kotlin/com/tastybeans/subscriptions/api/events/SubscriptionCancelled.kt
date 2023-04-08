package com.tastybeans.subscriptions.api.events

import java.time.LocalDate

data class SubscriptionCancelled(val customerId:String, val endDate: LocalDate) {
}