package com.example.getfood.ui.base;

public interface BaseMvpPresenter<V extends BaseView> {

    void onAttach(V mvpView);

    void onDetach();
}