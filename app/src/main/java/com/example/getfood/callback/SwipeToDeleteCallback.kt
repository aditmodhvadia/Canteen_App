package com.example.getfood.callback

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.getfood.R
import com.example.getfood.ui.cart.CartDisplayAdapter

class SwipeToDeleteCallback constructor(private val mAdapter: CartDisplayAdapter) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    private val background: ColorDrawable = ColorDrawable(Color.RED)
    private val icon: Drawable? = ContextCompat.getDrawable(mAdapter.context,
            R.drawable.ic_delete)

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, viewHolder1: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDraw(c, recyclerView, viewHolder, dX,
                dY, actionState, isCurrentlyActive)
        val itemView: View = viewHolder.itemView
        val backgroundCornerOffset = 20
        val iconMargin: Int = (itemView.height - icon!!.intrinsicHeight) / 2
        val iconTop: Int = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
        val iconBottom: Int = iconTop + icon.intrinsicHeight
        if (dX < 0) { // Swiping to the left
            val iconLeft: Int = itemView.right - iconMargin - icon.intrinsicWidth
            val iconRight: Int = itemView.right - iconMargin
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            background.setBounds(itemView.right + (dX.toInt()) - backgroundCornerOffset,
                    itemView.top, itemView.right, itemView.bottom)
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0)
        }
        background.draw(c)
        icon.draw(c)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
        val position: Int = viewHolder.adapterPosition
        mAdapter.deleteItem(position)
    }

}