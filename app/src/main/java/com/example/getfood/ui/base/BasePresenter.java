package com.example.getfood.ui.base;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BasePresenter<V extends BaseView> implements MvpPresenter<V> {

    private static final String TAG = "BasePresenter";

    private final CompositeDisposable mCompositeDisposable;

    private V mMvpView;

    public BasePresenter(CompositeDisposable compositeDisposable) {
        this.mCompositeDisposable = compositeDisposable;
    }

    @Override
    public void onAttach(V mvpView) {
        mMvpView = mvpView;
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

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.onAttach(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }
}