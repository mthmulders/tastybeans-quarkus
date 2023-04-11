package com.tastybeans.shipping.api.valueobjects

import com.tastybeans.shipping.domain.OrderItem

data class OrderItemInfo(val productId: Long, val description: String, val quantity:Int){
    companion object {
        fun from(orderItem: OrderItem): OrderItemInfo {
            return OrderItemInfo(orderItem.productId, orderItem.description, orderItem.quantity)
        }
    }
}