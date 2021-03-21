package com.example.getfood.ui.orderlist

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.canteen_app_models.models.FullOrder
import com.example.getfood.R
import com.example.getfood.ui.base.BaseActivity
import java.util.*

class OrderListActivity : BaseActivity(), OrderListMvpView {
    private var orderListDisplayAdapter: OrderListDisplayAdapter? = null
    override fun initViews() {
        showLoading()
        val ordersListRecyclerView = findViewById<RecyclerView>(R.id.ordersListRecyclerView)
        ordersListRecyclerView.layoutManager = LinearLayoutManager(this)
        ordersListRecyclerView.addItemDecoration(DividerItemDecoration(ordersListRecyclerView.context, LinearLayoutManager.VERTICAL))

//        ordersHeadingTextView = findViewById(R.id.ordersHeadingTextView);
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.title = getString(R.string.your_orders)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        val presenter = OrderListPresenter<OrderListActivity>()
        presenter.onAttach(this)
        orderListDisplayAdapter = OrderListDisplayAdapter(this)
        ordersListRecyclerView.adapter = orderListDisplayAdapter

        //        fetch all the order IDs of the user first
        presenter.fetchOrderList()
    }

    override fun setListeners() {}
    override val layoutResId: Int
        get() = R.layout.activity_order_list

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_order_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun bindListAdapter(orderListItems: ArrayList<FullOrder?>?) {
        orderListDisplayAdapter!!.swapData(orderListItems)
        hideLoading()
    }

    override fun onRollNumberNull() {
//        TODO: Either refresh state of Auth or redirect to LoginActivity
    }
}