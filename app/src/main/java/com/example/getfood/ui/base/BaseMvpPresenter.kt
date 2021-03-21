package com.example.getfood.ui.base

interface BaseMvpPresenter<V : BaseView?> {
    fun onAttach(mvpView: V)
    fun onDetach()
}