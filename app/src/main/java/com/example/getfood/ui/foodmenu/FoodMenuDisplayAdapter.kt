package com.example.getfood.ui.foodmenu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.canteen_app_models.models.FoodItem
import com.example.getfood.R
import com.example.getfood.callback.FoodItemTouchListener
import com.example.getfood.utils.AppUtils
import java.util.*

class FoodMenuDisplayAdapter internal constructor(private val context: Context, private val itemTouchListener: FoodItemTouchListener) : ListAdapter<FoodItem, FoodMenuDisplayAdapter.ViewHolder>(FoodMenuDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.food_menu_display_view_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        holder.bind(getItem(holder.adapterPosition))
    }

    fun swapData(newList: ArrayList<FoodItem?>?) {
        submitList(newList)
    }

    internal class FoodMenuDiffCallBack : DiffUtil.ItemCallback<FoodItem>() {
        override fun areItemsTheSame(foodItem: FoodItem, t1: FoodItem): Boolean {
            return foodItem.itemName == t1.itemName
        }

        override fun areContentsTheSame(foodItem: FoodItem, t1: FoodItem): Boolean {
            return foodItem == t1
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemNameTextView: TextView = itemView.findViewById(R.id.itemNameTextView)
        var itemPriceTextView: TextView = itemView.findViewById(R.id.itemPriceTextView)
        var itemRatingTextView: TextView = itemView.findViewById(R.id.itemRatingTextView)
        var ivRatingStar: ImageView = itemView.findViewById(R.id.ivRatingStar)
        fun bind(item: FoodItem?) {
            itemNameTextView.text = item!!.itemName
            itemPriceTextView.text = String.format("%s %s", context.getString(R.string.rupee_symbol),
                    item.itemPrice)
            if (item.itemRating != -1L) {
                itemRatingTextView.text = item.itemRating.toString()
                itemRatingTextView.setTextColor(AppUtils.getColorForRating(context, item.itemRating.toString()))
            } else {
                itemRatingTextView.visibility = View.INVISIBLE
                ivRatingStar.visibility = View.INVISIBLE
            }
            itemView.setOnClickListener { itemTouchListener.onItemClicked(adapterPosition, item) }
        }

    }
}