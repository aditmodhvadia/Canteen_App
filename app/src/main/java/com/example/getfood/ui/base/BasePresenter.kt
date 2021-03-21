package com.example.getfood.ui.base

import com.example.firebase_api_library.api.FireBaseApiManager
import com.example.firebase_api_library.api.FireBaseApiManager.Companion.instance
import com.example.getfood.data.DataManager

abstract class BasePresenter<V : BaseView?> : BaseMvpPresenter<V> {
    var apiManager: FireBaseApiManager? = null
        protected set
    protected lateinit var dataManager: DataManager
    var mvpView: V? = null
        private set

    override fun onAttach(mvpView: V) {
        this.mvpView = mvpView
        apiManager = instance
        dataManager = DataManager.instance
    }

    override fun onDetach() {
// mCompositeDisposable.dispose();
        mvpView!!.hideLoading()
        mvpView = null
    }

    val isViewAttached: Boolean
        get() = mvpView != null

    fun checkViewAttached() {
        if (!isViewAttached) throw MvpViewNotAttachedException()
    }

    class MvpViewNotAttachedException : RuntimeException("Please call Presenter.onAttach(MvpView) before" +
            " requesting data to the Presenter")

    companion object {
        private const val TAG = "BasePresenter"
    }
}