package com.tastybeans.subscriptions.api.commands

import java.time.LocalDate

data class CancelSubscription(val customerId:String, val endDate: LocalDate) {

}
