package com.example.charge;

public interface BaseView<T extends BasePresenter> {

    void setPresenter(T presenter);
}
