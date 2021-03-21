package com.example.getfood.ui.orderdetail

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.RatingBar.OnRatingBarChangeListener
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.canteen_app_models.models.CartItem
import com.example.getfood.R

class OrderDetailDisplayAdapter internal constructor(private val onOrderItemClickListener: OnOrderItemClickListener) : ListAdapter<CartItem, OrderDetailDisplayAdapter.ViewHolder>(OrderDetailDiffCallBack()) {
    fun swapData(newList: List<CartItem?>?) {
        submitList(newList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.order_display_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    internal interface OnOrderItemClickListener {
        fun onRatingGiven(rating: Float, position: Int)
    }

    internal class OrderDetailDiffCallBack : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(cartItem: CartItem, t1: CartItem): Boolean {
            return cartItem.itemName == t1.itemName
        }

        override fun areContentsTheSame(cartItem: CartItem, t1: CartItem): Boolean {
            return cartItem == t1
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var orderItemQuantityTextView: TextView = itemView.findViewById(R.id.orderItemQuantityTextView)
        private var orderItemNameTextView: TextView = itemView.findViewById(R.id.orderItemNameTextView)
        private var orderItemStatusTextView: TextView = itemView.findViewById(R.id.orderItemStatusTextView)
        private var ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        fun bind(item: CartItem) {
            if (item.itemName != null) {
                orderItemNameTextView.text = item.itemName
            }
            if (item.itemQuantity != null) {
                orderItemQuantityTextView.text = item.itemQuantity.toString()
            }
            if (item.itemStatus != null) {
                orderItemStatusTextView.text = item.itemStatus
            }
            if (item.itemRating != 0L) {
                ratingBar.rating = item.itemRating.toFloat()
                ratingBar.isEnabled = false
            } else {
                ratingBar.rating = 0f
                ratingBar.isEnabled = true
            }
            ratingBar.onRatingBarChangeListener = OnRatingBarChangeListener { ratingBar, _, fromUser ->
                if (fromUser) {
                    Log.d("##DebugData", "Rating is " + ratingBar.rating)
                    ratingBar.isEnabled = false
                    onOrderItemClickListener.onRatingGiven(ratingBar.rating, adapterPosition)
                }
            }
        }

    }
}