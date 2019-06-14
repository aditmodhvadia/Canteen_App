package com.example.getfood.ui.base;

import com.example.getfood.api.FireBaseApiManager;
import com.example.getfood.data.DataManager;

public abstract class BasePresenter<V extends BaseView> implements BaseMvpPresenter<V> {

    private static final String TAG = "BasePresenter";
    protected FireBaseApiManager apiManager;
    protected DataManager dataManager;
    private V mMvpView;

    public BasePresenter() {
    }

    @Override
    public void onAttach(V mvpView) {
        mMvpView = mvpView;
        apiManager = FireBaseApiManager.getInstance();
        dataManager = DataManager.getInstance();
    }

    public FireBaseApiManager getApiManager() {
        return apiManager;
    }

    @Override
    public void onDetach() {
// mCompositeDisposable.dispose();
        mMvpView.hideLoading();
        mMvpView = null;
    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public V getMvpView() {
        return mMvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.onAttach(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }
}