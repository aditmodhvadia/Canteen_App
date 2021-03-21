package com.example.getfood.ui.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.canteen_app_models.models.CartItem
import com.example.getfood.R
import com.example.getfood.callback.CartItemTouchListener
import com.example.getfood.utils.AppUtils
import com.google.android.material.snackbar.Snackbar
import java.util.*

class CartDisplayAdapter internal constructor(val context: Context, private val cartItemTouchListener: CartItemTouchListener) : ListAdapter<CartItem, CartDisplayAdapter.ViewHolder>(CartDiffCallBack()) {
    private lateinit var removedItem: CartItem
    private var mRecentlyDeletedItemPosition = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.cart_display_view_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun swapData(newList: List<CartItem?>?) {
        submitList(newList)
    }

    fun deleteItem(position: Int) {
        mRecentlyDeletedItemPosition = position
        removedItem = getItem(mRecentlyDeletedItemPosition)
        cartItemTouchListener.onItemRemoved(position)
        showUndoSnackBar()
    }

    private fun showUndoSnackBar() {
        val snackBar: Snackbar = AppUtils.getSnackBar(context, context.getString(R.string.item_remove))
        snackBar.setActionTextColor(ContextCompat.getColor(context, R.color.snackbar_yellow))
        snackBar.setAction(R.string.undo) { undoDelete() }
        snackBar.show()
    }

    private fun undoDelete() {
        cartItemTouchListener.onItemRemoveUndo(removedItem, mRecentlyDeletedItemPosition)
    }

    val cartItemsCount: Int
        get() = itemCount

    fun itemInserted(position: Int) {
        notifyItemInserted(position)
    }

    fun itemRemoved(position: Int) {
        notifyItemRemoved(position)
    }

    fun itemChanged(position: Int) {
        notifyItemChanged(position)
    }

    internal class CartDiffCallBack : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(cartItem: CartItem, t1: CartItem): Boolean {
            return cartItem.itemName == t1.itemName
        }

        override fun areContentsTheSame(cartItem: CartItem, t1: CartItem): Boolean {
            return cartItem == t1
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var itemQuantityTextView: TextView = itemView.findViewById(R.id.itemQuantityTextView)
        private var itemNameTextView: TextView = itemView.findViewById(R.id.itemNameTextView)
        private var itemPriceTextView: TextView = itemView.findViewById(R.id.itemPriceTextView)
        private var increaseButton: ImageButton = itemView.findViewById(R.id.increaseButton)
        private var decreaseButton: ImageButton = itemView.findViewById(R.id.decreaseButton)
        fun bind(item: CartItem?) {
            itemNameTextView.text = item!!.itemName
            itemPriceTextView.text = String.format(Locale.ENGLISH, "%s%d", context.getString(R.string.rupee_symbol), item.itemPrice!!.toInt())
            itemQuantityTextView.text = item.itemQuantity.toString()
            increaseButton.setOnClickListener { /*int position = holder.getAdapterPosition();
                int value = FoodMenuDisplayActivity.cartItems.get(position).getItemQuantity();
                if (value < 10) {
                    FoodMenuDisplayActivity.cartItems.get(position).increaseQuantity();
                    CartActivity.calcTotal();
                    notifyItemChanged(position);
                    Toast.makeText(context, context.getString(R.string.adjust_cart), Toast.LENGTH_SHORT).show();
                }*/
                cartItemTouchListener.onIncreaseClicked(adapterPosition)
            }
            decreaseButton.setOnClickListener { /*int position = holder.getAdapterPosition();

                if (position != RecyclerView.NO_POSITION) {

                    int value = FoodMenuDisplayActivity.cartItems.get(position).getItemQuantity();

                    if (value > 1) {
                        FoodMenuDisplayActivity.cartItems.get(position).decreaseQuantity();
                        notifyItemChanged(position);
                        CartActivity.calcTotal();
                        Toast.makeText(context, context.getString(R.string.adjust_cart), Toast.LENGTH_SHORT).show();
                    } else if (value == 1) {

                        FoodMenuDisplayActivity.cartItems.remove(position);
                        if (FoodMenuDisplayActivity.cartItems.size() == 0) {
                            Toast.makeText(context, context.getString(R.string.cart_empty), Toast.LENGTH_SHORT).show();
                            CartActivity.activity.finish();
                        }
                        notifyItemRemoved(position);
                        CartActivity.calcTotal();
                        Toast.makeText(context, context.getString(R.string.adjust_cart), Toast.LENGTH_SHORT).show();

                    }
                }*/
                if (item.itemQuantity == 1) {
                    cartItemTouchListener.onItemRemoved(adapterPosition)
                } else {
                    cartItemTouchListener.onDecreaseClicked(adapterPosition)
                }
            }
        }

    }
}