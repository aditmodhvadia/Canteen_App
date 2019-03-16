package com.example.getfood.ui.base;

public interface MvpPresenter<V extends BaseView> {

    void onAttach(V mvpView);

    void onDetach();
}