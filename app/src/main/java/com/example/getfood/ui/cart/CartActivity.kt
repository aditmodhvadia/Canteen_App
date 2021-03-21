package com.example.getfood.ui.cart

import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.canteen_app_models.models.CartItem
import com.example.canteen_app_models.models.FullOrder
import com.example.getfood.Paytm
import com.example.getfood.R
import com.example.getfood.callback.CartItemTouchListener
import com.example.getfood.callback.SwipeToDeleteCallback
import com.example.getfood.ui.base.BaseActivity
import com.example.getfood.ui.orderdetail.OrderDetailActivity
import com.example.getfood.utils.AppUtils
import com.example.getfood.utils.alert.AlertUtils
import com.example.getfood.utils.alert.DialogConfirmation.ConfirmationDialogListener
import com.example.getfood.utils.alert.DialogSimple
import com.paytm.pgsdk.PaytmOrder
import com.paytm.pgsdk.PaytmPGService
import com.paytm.pgsdk.PaytmPaymentTransactionCallback
import org.json.JSONException
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class CartActivity : BaseActivity(), CartMvpView, View.OnClickListener, CartItemTouchListener, PaytmPaymentTransactionCallback {
    private lateinit var totalPriceTV: TextView
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var orderButton: Button
    private lateinit var adapter: CartDisplayAdapter
    private lateinit var firstBreakButton: Button
    private lateinit var secondBreakButton: Button
    private lateinit var lastBreakButton: Button
    private lateinit var nowButton: Button
    private var orderTime: String? = null
    private lateinit var chooseTimeDialog: AlertDialog
    private lateinit var tvCurrentDate: TextView
    private lateinit var presenter: CartPresenter<CartActivity>
    override val layoutResId: Int
        get() = R.layout.activity_cart

    override fun initViews() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setTitle(R.string.my_cart)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            val upArrow = ResourcesCompat.getDrawable(resources, R.drawable.ic_down, null)
            supportActionBar!!.setHomeAsUpIndicator(upArrow)
        }
        cartRecyclerView = findViewById(R.id.cartRecyclerView)
        cartRecyclerView.setHasFixedSize(true)
        cartRecyclerView.setLayoutManager(LinearLayoutManager(this))
        cartRecyclerView.addItemDecoration(DividerItemDecoration(cartRecyclerView.context, LinearLayoutManager.VERTICAL))
        totalPriceTV = findViewById(R.id.totalPriceTV)
        orderButton = findViewById(R.id.orderButton)
        presenter = CartPresenter()
        presenter.onAttach(this)
        adapter = CartDisplayAdapter(this, this)
        adapter.swapData(presenter.cartItems)
        cartRecyclerView.adapter = adapter
        updateCartTotal()
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(adapter))
        itemTouchHelper.attachToRecyclerView(cartRecyclerView)
    }

    private fun updateCartTotal() {
        totalPriceTV.alpha = 0.0f
        totalPriceTV.text = presenter.cartTotal.toString()
        totalPriceTV.animate().alpha(1.0f).duration = 250
    }

    override fun setListeners() {
        orderButton.setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_cart, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) onBackPressed()
        if (id == R.id.menu_clear_cart) {
            AlertUtils.showConfirmationDialog(this, getString(R.string.clear_cart),
                    getString(R.string.sure_clear_cart), getString(R.string.yes), getString(R.string.no), object : ConfirmationDialogListener {
                override fun onPositiveButtonClicked() {
                    clearCart()
                    makeText(getString(R.string.cart_cleared))
                    onBackPressed()
                }

                override fun onNegativeButtonClicked() {}
            })
        }
        return super.onOptionsItemSelected(item)
    }

    private fun clearCart() {
        presenter.clearCartItems()
    }

    private fun chooseTime() {
        val chooseTimeBuilder = AlertDialog.Builder(this@CartActivity)
        chooseTimeBuilder.setTitle(R.string.choose_time)
        val chooseTimeView = layoutInflater.inflate(R.layout.choose_time_order, null)
        firstBreakButton = chooseTimeView.findViewById(R.id.firstBreakButton)
        secondBreakButton = chooseTimeView.findViewById(R.id.secondBreakButton)
        lastBreakButton = chooseTimeView.findViewById(R.id.lastBreakButton)
        nowButton = chooseTimeView.findViewById(R.id.nowButton)
        tvCurrentDate = chooseTimeView.findViewById(R.id.tvCurrentDate)

//        TODO: Confirm timings with Canteen
        val currTime: Calendar = Calendar.getInstance()
        tvCurrentDate.text = AppUtils.todaysDate
        //        check if it is a sunday, then ordering is closed TODO: confirm with docs for the value of Sunday in Calendar
        currTime[Calendar.DAY_OF_WEEK] = currTime[Calendar.DAY_OF_WEEK] - 1
        //        if (currTime.get(Calendar.DAY_OF_WEEK) == 7) {
//            makeText("Cannot order on Sunday, Order Tomorrow");
//            return;
//        }
        val hour = 12
        val mins = 15
        if (hour <= 8 && mins <= 20) {
            Log.d("Debug", "Before Ordering time")
            makeText("Cannot place order now, Order after 08:20 AM")
            return
        } else if (hour <= 8 && mins <= 45) {
            Log.d("Debug", "Between 8:20 and 8:45")
            nowButton.setEnabled(false)
            nowButton.setBackgroundResource(R.drawable.button_disabled)
        } else if (hour < 10 || hour == 10 && mins <= 50) {
            Log.d("Debug", "Before first break")
        } else if (hour < 13 || hour == 13 && mins < 15) {
            Log.d("Debug", "Before second break")
            firstBreakButton.setEnabled(false)
            firstBreakButton.setBackgroundResource(R.drawable.button_disabled)
        } else if (hour < 16 || hour == 16 && mins < 15) {
            Log.d("Debug", " time")
            firstBreakButton.setEnabled(false)
            secondBreakButton.setEnabled(false)
            firstBreakButton.setBackgroundResource(R.drawable.button_disabled)
            secondBreakButton.setBackgroundResource(R.drawable.button_disabled)
        } else if (hour < 17 || hour == 17 && mins <= 45) {
            Log.d("Debug", "after 4:15")
            firstBreakButton.setEnabled(false)
            secondBreakButton.setEnabled(false)
            lastBreakButton.setEnabled(false)
            firstBreakButton.setBackgroundResource(R.drawable.button_disabled)
            secondBreakButton.setBackgroundResource(R.drawable.button_disabled)
            lastBreakButton.setBackgroundResource(R.drawable.button_disabled)
        } else {
            makeText("Cannot place order now, Order tomorrow")
            return
        }
        chooseTimeBuilder.setView(chooseTimeView)
        firstBreakButton.setOnClickListener(this)
        secondBreakButton.setOnClickListener(this)
        lastBreakButton.setOnClickListener(this)
        nowButton.setOnClickListener(this)
        chooseTimeBuilder.setNegativeButton(R.string.cancel) { _, _ -> }
        chooseTimeDialog = chooseTimeBuilder.show()
        val nbutton = chooseTimeDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        nbutton.setTextColor(ResourcesCompat.getColor(resources, R.color.colorPrimary, null))
    }

    override fun onStart() {
        super.onStart()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

    override fun onPostResume() {
        super.onPostResume()
        if (presenter.cartItems.size == 0) {
            finish()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.orderButton -> chooseTime()
            R.id.firstBreakButton -> {
                orderTime = firstBreakButton.text.toString()
                placeOrder()
            }
            R.id.secondBreakButton -> {
                orderTime = secondBreakButton.text.toString()
                placeOrder()
            }
            R.id.lastBreakButton -> {
                orderTime = lastBreakButton.text.toString()
                placeOrder()
            }
            R.id.nowButton -> {
                val date = Date()
                val strDateFormat = getString(R.string.date_format)
                val dateFormat: DateFormat = SimpleDateFormat(strDateFormat, Locale.ENGLISH)
                orderTime = dateFormat.format(date)
                placeOrder()
            }
            else -> chooseTime()
        }
        //Before placing the order, PayTM Gateway will be called and the code will be shifted into it's method
    }

    private fun placeOrder() {
        chooseTimeDialog.dismiss()
        showLoading()

//        TODO: generate unique Order ID and redirect user to Order Activity, before that accept the payment.

//        check for network connectivity and proceed with order
        val cm = applicationContext.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting
        if (isConnected) {
            generateOrder()
        } else {
            hideLoading()
            makeText(getString(R.string.no_internet))
        }
    }

    private fun generateOrder() {

//        TODO: make object of FullOrder with all the fields
        presenter.sortCartItems()
        val orderId = presenter.newOrderKey
        if (orderId != null) {
            val fullOrder = FullOrder(presenter.cartItems, presenter.cartTotal.toString(),
                    orderTime, presenter.rollNo, orderId, null)
            presenter.setNewOrder(fullOrder)
        } else {
            hideLoading()
            AlertUtils.showAlertBox(this, "Some Error occurred!",
                    "Please try again later", getString(R.string.ok), object : DialogSimple.AlertDialogListener {
                override fun onButtonClicked() {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    override fun onIncreaseClicked(adapterPosition: Int) {
        presenter.increaseCartQuantity(adapterPosition)
        updateCartTotal()
        adapter.itemChanged(adapterPosition)
        Toast.makeText(this, getString(R.string.adjust_cart), Toast.LENGTH_SHORT).show()
    }

    override fun onDecreaseClicked(adapterPosition: Int) {
        presenter.decreaseCartItemQuantity(adapterPosition)
        updateCartTotal()
        Toast.makeText(this, getString(R.string.adjust_cart), Toast.LENGTH_SHORT).show()
        adapter.itemChanged(adapterPosition)
    }

    override fun onItemRemoved(adapterPosition: Int) {
        presenter.removeCartItem(adapterPosition)
        updateCartTotal()
        adapter.itemRemoved(adapterPosition)
        if (adapter.cartItemsCount == 0) {
            finish()
        }
    }

    override fun onItemRemoveUndo(removedItem: CartItem, mRecentlyDeletedItemPosition: Int) {
        presenter.undoCartItemRemove(removedItem, mRecentlyDeletedItemPosition)
        updateCartTotal()
        adapter.itemInserted(mRecentlyDeletedItemPosition)
    }

    override fun onOrderPlacedSuccessfully(orderId: String?) {
        presenter.getOrderData(orderId)
    }

    override fun onOrderDataFetchedSuccessfully(fullOrder: FullOrder?) {
        hideLoading()
        if (fullOrder != null) {
            hideLoading()
            val orderIntent = Intent(this@CartActivity, OrderDetailActivity::class.java)
            orderIntent.putExtra("TestOrderData", fullOrder)
            startActivity(orderIntent)
        }
    }

    override fun onOrderFailed(error: Error?) {
        AlertUtils.showAlertBox(this, "Some error occurred!", error!!.message,
                getString(R.string.ok), object : DialogSimple.AlertDialogListener {
            override fun onButtonClicked() {
                TODO("Not yet implemented")
            }
        })
    }

    private fun generateCheckSumVoley() {
        val queue = Volley.newRequestQueue(this)
        val map = HashMap<String?, String?>()
        map[getString(R.string.mid)] = "GetFoo88084336099945"
        map[getString(R.string.order_id)] = "osltry"
        map[getString(R.string.cust_id)] = "15bce001"
        map[getString(R.string.industry_id)] = getString(R.string.retail)
        map[getString(R.string.channel_id)] = getString(R.string.wap)
        map[getString(R.string.txn_amt)] = total.toString()
        map[getString(R.string.website)] = getString(R.string.staging)
        map[getString(R.string.callback)] = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=osltry"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, "https://adit-canteen-alay1012.c9users.io/paytm/generateChecksum.php", JSONObject(map as Map<*, *>), { response ->
            try {
//                            makeText(response.getString("CHECKSUMHASH"));
                Log.d("response", "Our server Checksum was " + response.getString(getString(R.string.checksum)))
                //                            call paytm activity with the checksum received
                initializePaytmPayment(response.getString(getString(R.string.checksum)), null)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }) {
            makeText("Did not work")
            //                        Log.d("newerror", error.networkResponse.toString());
        }
        queue.add(jsonObjectRequest)
        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
    }

    private fun initializePaytmPayment(checksumHash: String, paytm: Paytm?) {

        //getting paytm service
        val Service = PaytmPGService.getStagingService()

        //use this when using for production
        //PaytmPGService Service = PaytmPGService.getProductionService();

        //creating a hashmap and adding all the values required
//        dummy values as of now for testing purposes
        val paramMap: MutableMap<String, String> = HashMap()
        paramMap[getString(R.string.mid)] = getString(R.string.mid_value)
        paramMap[getString(R.string.order_id)] = "osltry"
        paramMap[getString(R.string.cust_id)] = "15bce001"
        paramMap[getString(R.string.industry_id)] = getString(R.string.retail)
        paramMap[getString(R.string.channel_id)] = getString(R.string.wap)
        paramMap[getString(R.string.txn_amt)] = total.toString()
        paramMap[getString(R.string.website)] = getString(R.string.staging)
        paramMap[getString(R.string.callback)] = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=osltry"
        paramMap[getString(R.string.checksum)] = checksumHash


        //creating a paytm order object using the hashmap
        val order = PaytmOrder(paramMap)

        //intializing the paytm service
        Service.initialize(order, null)

        //finally starting the payment transaction
        Service.startPaymentTransaction(this, true, true, this)
    }

    //all these overriden method is to detect the payment result accordingly
    override fun onTransactionResponse(bundle: Bundle) {

//        Toast.makeText(this, bundle.toString(), Toast.LENGTH_LONG).show();
        Log.d("PayTM", bundle.toString())
        Log.d("response", "Checksum from Paytm server was " + bundle.getString(getString(R.string.checksum)))
        clearCart()
    }

    override fun networkNotAvailable() {
        Toast.makeText(this, getString(R.string.network_error), Toast.LENGTH_LONG).show()
    }

    override fun clientAuthenticationFailed(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
        Log.d("PayTM", s)
    }

    override fun someUIErrorOccurred(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
        Log.d("PayTM", s)
    }

    override fun onErrorLoadingWebPage(i: Int, s: String, s1: String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
        Log.d("PayTM", "$s $s1")
    }

    override fun onBackPressedCancelTransaction() {
        Toast.makeText(this, getString(R.string.back_pressed), Toast.LENGTH_LONG).show()
    }

    override fun onTransactionCancel(s: String, bundle: Bundle) {
        Toast.makeText(this, s + bundle.toString(), Toast.LENGTH_LONG).show()
        Log.d("PayTM", bundle.toString())
    }

    fun makeText(msg: String?) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    companion object {
        var total = 0
    }
}