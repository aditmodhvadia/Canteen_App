package com.example.canteen_app_models.models

import com.example.canteen_app_models.helpers.FoodMenuDetails
import java.io.Serializable
import java.util.*

open class FoodItem(
        var itemName: String? = null,
        var itemPrice: String? = null,
        var itemCategory: String? = null,
        var itemRating: Long = 0,
) : Serializable {

    companion object {
        fun fromMap(dsp: MutableMap<String, Any>?, category: String?, itemName: String?): FoodItem? {
            val map = dsp as HashMap<String, Any>?
            return if (map != null) {
                var rating: Long = -1
                if (map.containsKey(FoodMenuDetails.RATING)) {
                    rating = map[FoodMenuDetails.RATING] as Long
                }
                FoodItem(
                        itemName, map[FoodMenuDetails.PRICE].toString(),
                        category,
                        rating,
                )
            } else {
                null
            }
        }
    }
}