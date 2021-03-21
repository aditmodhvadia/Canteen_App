package com.example.canteen_app_models.models

import java.io.Serializable

/**
 * It is the complete order fetched directly from the FireBase Real-time database
 */
data class FullOrder(
        var orderItems: MutableList<CartItem>? = null,
        var orderAmount: String? = null,
        var timeToDeliver: String? = null,
        var rollNo: String? = null,
        var orderId: String? = null,
        var orderStatus: String? = null,
        val displayID: String? = null,
) : Serializable