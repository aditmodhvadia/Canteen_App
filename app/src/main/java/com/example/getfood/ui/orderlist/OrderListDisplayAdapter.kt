package com.example.getfood.ui.orderlist

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.canteen_app_models.models.FullOrder
import com.example.getfood.R
import com.example.getfood.ui.orderdetail.OrderDetailActivity

class OrderListDisplayAdapter internal constructor(private val context: Context) : ListAdapter<FullOrder, OrderListDisplayAdapter.ViewHolder>(OrderListDiffCallBack()) {
    fun swapData(newList: List<FullOrder?>?) {
        submitList(newList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.order_list_display_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    internal class OrderListDiffCallBack : DiffUtil.ItemCallback<FullOrder>() {
        override fun areItemsTheSame(order: FullOrder, t1: FullOrder): Boolean {
            return order.orderId == t1.orderId
        }

        override fun areContentsTheSame(order: FullOrder, t1: FullOrder): Boolean {
            return order == t1
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var orderIDTextView: TextView = itemView.findViewById(R.id.orderIDTextView)
        private var orderAmountTextView: TextView = itemView.findViewById(R.id.orderAmountTextView)
        private var orderTimeTextView: TextView = itemView.findViewById(R.id.orderTimeTextView)
        fun bind(item: FullOrder?) {
            val orderId: String? = if (item!!.displayID != null &&
                    item.displayID!!.isNotEmpty()) {
                item.displayID
            } else {
                item.orderId
            }
            orderIDTextView.text = String.format("Order ID: %s", orderId)
            orderAmountTextView.text = String.format("Amount: ₹ %s",
                    item.orderAmount)
            orderTimeTextView.text = item.timeToDeliver
            itemView.setOnClickListener {
                val i = Intent(context, OrderDetailActivity::class.java)
                i.putExtra("TestOrderData", item)
                context.startActivity(i)
            }
        }

    }
}