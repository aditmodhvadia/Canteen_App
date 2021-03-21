package com.example.getfood.ui.orderdetail

import android.content.*
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.canteen_app_models.models.FullOrder
import com.example.getfood.R
import com.example.getfood.ui.base.BaseActivity
import com.example.getfood.ui.orderdetail.OrderDetailDisplayAdapter.OnOrderItemClickListener
import com.example.getfood.utils.AppUtils
import com.example.getfood.utils.alert.AlertUtils
import com.example.getfood.utils.alert.DialogSimple

class OrderDetailActivity : BaseActivity(), OrderDetailMvpView, OnOrderItemClickListener {
    private var testTV: TextView? = null
    private var orderDisplayAdapter: OrderDetailDisplayAdapter? = null
    private lateinit var orderData: Intent
    private var presenter: OrderDetailPresenter<OrderDetailActivity>? = null
    private var fullOrder: FullOrder? = null
    override fun initViews() {
        testTV = findViewById(R.id.testTV)
        val orderRecyclerView = findViewById<RecyclerView>(R.id.orderDetailRecyclerView)
        orderRecyclerView.layoutManager = LinearLayoutManager(context)
        orderRecyclerView.addItemDecoration(DividerItemDecoration(orderRecyclerView.context, LinearLayoutManager.VERTICAL))
        presenter = OrderDetailPresenter()
        presenter!!.onAttach(this)

//        Getting data from the calling activity/Intent
        orderData = intent
        if (orderData.extras == null || orderData.extras!!.isEmpty) {
            AlertUtils.showAlertBox(context, getString(R.string.std_error_title), getString(R.string.std_error_msg),
                    getString(R.string.ok), object : DialogSimple.AlertDialogListener {
                override fun onButtonClicked() {
                    finish()
                }
            })
            return
        }
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        AppUtils.createNotificationChannel(context)
        showLoading()
        //        Get data from Intent
        fullOrder = intent.getSerializableExtra("TestOrderData") as FullOrder?

//        orderDisplayAdapter = new OrderDetailDisplayAdapter(fullOrder, context, this);
        orderDisplayAdapter = OrderDetailDisplayAdapter(this)
        orderRecyclerView.adapter = orderDisplayAdapter
        //        Log.d("##DebugData", fullOrder.toString());
        presenter!!.fetchOrderDetails(fullOrder!!)
    }

    override fun setListeners() {}
    override val layoutResId: Int
        get() = R.layout.activity_order_detail
    val context: Context
        get() = this

    override fun bindOrderDetailAdapter(updatedOrder: FullOrder) {
        fullOrder = updatedOrder
        if (updatedOrder.displayID != null) {
            testTV!!.text = String.format("%s %s", getString(R.string.order_id_is), updatedOrder.displayID)
        } else {
            testTV!!.text = String.format("%s %s", getString(R.string.order_id_is), updatedOrder.orderId)
        }
        Log.i("OrderDetailActivity", "Called submit list")
        orderDisplayAdapter!!.swapData(updatedOrder.orderItems)
        hideLoading()
    }

    override fun onDatabaseError(error: Error?) {
        Toast.makeText(this@OrderDetailActivity, error!!.message, Toast.LENGTH_SHORT).show()
    }

    override fun onRatingUpdatedSuccessfully() {
        Toast.makeText(this@OrderDetailActivity, getString(R.string.rating_saved), Toast.LENGTH_SHORT).show()
    }

    override fun onRatingUpdateFailed(error: Error) {
        Toast.makeText(this@OrderDetailActivity, error.message, Toast.LENGTH_SHORT).show()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        orderData = intent
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onRatingGiven(rating: Float, position: Int) {
        presenter!!.setRatingValueForOrderItem(rating, position, fullOrder!!.orderId)
    }
}