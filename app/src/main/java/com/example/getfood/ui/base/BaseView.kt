package com.example.getfood.ui.base

interface BaseView {
    fun showLoading()
    fun hideLoading()
    val isNetworkConnected: Boolean
}