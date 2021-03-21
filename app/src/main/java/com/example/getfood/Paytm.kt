package com.example.getfood

import android.util.Log
import com.google.gson.annotations.SerializedName
import java.util.*

class Paytm constructor(mId: String?, channelId: String?, @field:SerializedName("TXN_AMOUNT") var txnAmount: String, website: String?, callBackUrl: String?, industryTypeId: String?) {
    @SerializedName("MID")
    var mId: String = "GetFoo88084336099945"

    @SerializedName("ORDER_ID")
    var orderId: String = "voleyfinal"

    @SerializedName("CUST_ID")
    var custId: String = "15bce001"

    @SerializedName("CHANNEL_ID")
    var channelId: String = "WAP"

    @SerializedName("WEBSITE")
    var website: String = "APPSTAGING"

    @SerializedName("CALLBACK_URL")
    var callBackUrl: String = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=voleyfinal"

    @SerializedName("INDUSTRY_TYPE_ID")
    var industryTypeId: String = "Retail"
    fun getmId(): String {
        return mId
    }

    /*
     * The following method we are using to generate a random string everytime
     * As we need a unique customer id and order id everytime
     * For real scenario you can implement it with your own application logic
     * */
    private fun generateString(): String {
        val uuid: String = UUID.randomUUID().toString()
        return uuid.replace("-".toRegex(), "")
    }

    init {
        Log.d("orderId", orderId)
        Log.d("customerId", custId)
    }
}